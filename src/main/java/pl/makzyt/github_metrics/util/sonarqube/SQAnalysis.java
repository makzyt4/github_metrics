package pl.makzyt.github_metrics.util.sonarqube;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SQAnalysis {
    private SonarQube sonarQube;
    private int bugs;
    private float classComplexity;

    protected SQAnalysis(SonarQube sonarQube, String projectKey)
            throws IOException {

        this.sonarQube = sonarQube;
        String analysisUrl = sonarQube.getHost() +
                "/api/measures/component?metricKeys=bugs,class_complexity" +
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

        for (JsonElement element : measures) {
            String key = element.getAsJsonObject().get("metric").getAsString();
            JsonElement value = element.getAsJsonObject().get("value");

            switch (key) {
                case "bugs":
                    bugs = value.getAsInt(); break;
                case "class_complexity":
                    classComplexity = value.getAsFloat(); break;
                default: break;
            }
        }
    }

    public int getBugs() {
        return bugs;
    }

    public float getClassComplexity() {
        return classComplexity;
    }
}
