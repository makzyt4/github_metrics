package pl.makzyt.github_metrics.util.sonarqube;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SQAnalysis {
    private SonarQube sonarQube;

    private int bugs;
    private int classes;
    private int codeSmells;
    private int cognitiveComplexity;
    private int commentLines;
    private int commentedOutCodeLines;
    private int complexity;
    private int complexityInClasses;
    private int complexityInFunctions;
    private int criticalViolations;
    private int developmentCost;
    private int directories;
    private int duplicatedFiles;
    private int duplicatedLines;
    private int files;
    private int lines;
    private int majorViolations;
    private int minorViolations;

    private float businessValue;
    private float classComplexity;
    private float commentLinesDensity;
    private float coverage;
    private float fileComplexity;
    private float functionComplexity;
    private float sqaleRating;

    private HashMap<String, Double> languageDistribution;

    private JsonArray languageObjs;

    protected SQAnalysis(SonarQube sonarQube, String projectKey)
            throws IOException {

        this.sonarQube = sonarQube;
        String analysisUrl = sonarQube.getHost() +
                "/api/measures/component?metricKeys=" +
                "bugs," +
                "class_complexity," +
                "ncloc_language_distribution," +
                "business_value," +
                "classes," +
                "code_smells," +
                "cognitive_complexity," +
                "comment_lines," +
                "commented_out_code_lines," +
                "comment_lines_density," +
                "file_complexity," +
                "function_complexity," +
                "complexity_in_classes," +
                "complexity_in_functions," +
                "coverage," +
                "critical_violations," +
                "complexity," +
                "development_cost," +
                "directories," +
                "duplicated_files," +
                "duplicated_lines," +
                "sqale_rating," +
                "files," +
                "lines" +
                "&componentKey=%s";
        analysisUrl = String.format(analysisUrl, projectKey);

        HttpGet httpGet = new HttpGet(analysisUrl);
        HttpResponse response = sonarQube.getClient().execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser jsonParser = new JsonParser();
        JsonObject rootObj = jsonParser.parse(jsonString).getAsJsonObject();

        JsonArray measures = rootObj
                .get("component")
                .getAsJsonObject()
                .get("measures")
                .getAsJsonArray();

        String nclocLangDist = "";

        // Load languages JSON
        String langListUrl = sonarQube.getHost() + "/api/languages/list";
        httpGet = new HttpGet(langListUrl);
        response = sonarQube.getClient().execute(httpGet);

        jsonString = EntityUtils.toString(response.getEntity());
        rootObj = jsonParser.parse(jsonString).getAsJsonObject();

        languageObjs = rootObj.get("languages").getAsJsonArray();

        for (JsonElement element : measures) {
            String key = element.getAsJsonObject().get("metric").getAsString();
            JsonElement value = element.getAsJsonObject().get("value");

            switch (key) {
                case "bugs":
                    bugs = value.getAsInt();
                    break;
                case "business_value":
                    businessValue = value.getAsFloat();
                    break;
                case "class_complexity":
                    classComplexity = value.getAsFloat();
                    break;
                case "classes":
                    classes = value.getAsInt();
                    break;
                case "code_smells":
                    codeSmells = value.getAsInt();
                    break;
                case "cognitive_complexity":
                    cognitiveComplexity = value.getAsInt();
                    break;
                case "comment_lines":
                    commentLines = value.getAsInt();
                    break;
                case "commented_out_code_lines":
                    commentedOutCodeLines = value.getAsInt();
                    break;
                case "comment_lines_density":
                    commentLinesDensity = value.getAsFloat();
                    break;
                case "file_complexity":
                    fileComplexity = value.getAsFloat();
                    break;
                case "function_complexity":
                    functionComplexity = value.getAsFloat();
                    break;
                case "complexity_in_classes":
                    complexityInClasses = value.getAsInt();
                    break;
                case "complexity_in_functions":
                    complexityInFunctions = value.getAsInt();
                    break;
                case "coverage":
                    coverage = value.getAsFloat();
                    break;
                case "critical_violations":
                    criticalViolations = value.getAsInt();
                    break;
                case "complexity":
                    complexity = value.getAsInt();
                    break;
                case "development_cost":
                    developmentCost = value.getAsInt();
                    break;
                case "ncloc_language_distribution":
                    nclocLangDist = value.getAsString();
                    break;
                case "directories":
                    directories = value.getAsInt();
                    break;
                case "duplicated_files":
                    duplicatedFiles = value.getAsInt();
                    break;
                case "duplicated_lines":
                    duplicatedLines = value.getAsInt();
                    break;
                case "sqale_rating":
                    sqaleRating = value.getAsFloat();
                    break;
                case "major_violations":
                    majorViolations = value.getAsInt();
                    break;
                case "minor_violations":
                    minorViolations = value.getAsInt();
                    break;
                case "files":
                    files = value.getAsInt();
                    break;
                case "lines":
                    lines = value.getAsInt();
                    break;
                default: break;
            }
        }

        // Get percentage od language distributions
        List<String> langDistValues = Arrays.asList(nclocLangDist.split(";"));

        long lines = 0;

        for (String pair : langDistValues) {
            lines += Long.parseLong(pair.split("=")[1]);
        }

        languageDistribution = new HashMap<>();

        for (String pair : langDistValues) {
            String[] splitPair = pair.split("=");

            String key = splitPair[0];

            double value = Double.parseDouble(splitPair[1]);
            value = value * 100.0 / lines;

            String langName = getLanguageName(key);

            languageDistribution.put(langName, value);
        }
    }

    private String getLanguageName(String key) throws IOException {
        for (JsonElement element : languageObjs) {
            String currentKey = element.getAsJsonObject().get("key").getAsString();

            if (currentKey.equals(key)) {
                return element.getAsJsonObject().get("name").getAsString();
            }
        }

        return "";
    }

    public int getBugs() {
        return bugs;
    }

    public float getClassComplexity() {
        return classComplexity;
    }

    public float getBusinessValue() {
        return businessValue;
    }

    public HashMap<String, Double> getLanguageDistribution() {
        return languageDistribution;
    }

    public int getClasses() {
        return classes;
    }

    public int getCodeSmells() {
        return codeSmells;
    }

    public int getCognitiveComplexity() {
        return cognitiveComplexity;
    }

    public float getCommentLinesDensity() {
        return commentLinesDensity;
    }

    public int getCommentedOutCodeLines() {
        return commentedOutCodeLines;
    }

    public int getCommentLines() {
        return commentLines;
    }

    public float getFileComplexity() {
        return fileComplexity;
    }

    public float getFunctionComplexity() {
        return functionComplexity;
    }

    public int getComplexityInClasses() {
        return complexityInClasses;
    }

    public int getComplexityInFunctions() {
        return complexityInFunctions;
    }

    public float getCoverage() {
        return coverage;
    }

    public int getCriticalViolations() {
        return criticalViolations;
    }

    public int getComplexity() {
        return complexity;
    }

    public int getDevelopmentCost() {
        return developmentCost;
    }

    public int getDirectories() {
        return directories;
    }

    public int getDuplicatedFiles() {
        return duplicatedFiles;
    }

    public int getDuplicatedLines() {
        return duplicatedLines;
    }

    public String getSqaleRating() {
        if (sqaleRating <= 1.0) {
            return "A";
        } else if (sqaleRating <= 2.0) {
            return "B";
        } else if (sqaleRating <= 3.0) {
            return "C";
        } else if (sqaleRating <= 4.0) {
            return "D";
        }

        return "E";
    }

    public int getMajorViolations() {
        return majorViolations;
    }

    public int getMinorViolations() {
        return minorViolations;
    }

    public int getFiles() {
        return files;
    }
}
