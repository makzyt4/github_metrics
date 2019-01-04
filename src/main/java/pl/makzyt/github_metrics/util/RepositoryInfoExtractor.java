package pl.makzyt.github_metrics.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepositoryInfoExtractor {
    private Logger logger = LoggerFactory.getLogger(RepositoryInfoExtractor.class);

    private final String userName;
    private final String repositoryName;

    public RepositoryInfoExtractor(String repositoryUrl) {
        final Pattern pattern = Pattern.compile("https?://(www\\.)?github\\.com/([a-zA-Z0-9\\-]+)/([a-zA-Z0-9\\-]+)/?");
        final Matcher matcher = pattern.matcher(repositoryUrl);

        // Get username and repository name from the URL.
        if (matcher.matches()) {
            this.userName = matcher.group(2);
            this.repositoryName = matcher.group(3);
        } else {
            throw new IllegalStateException();
        }
    }

    public HashMap<String, String> getLanguageDistribution() {
        final String languagesUrl = "https://api.github.com/repos/" + this.userName + "/" + this.repositoryName + "/languages";

        try {
            // Connect to API.
            URL url = new URL(languagesUrl);
            URLConnection request = null;
            request = url.openConnection();
            request.connect();

            // Get languages from JSON.
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootObject = root.getAsJsonObject();
            Set<String> keySet = rootObject.keySet();

            // Iterate the language key set, sum the lines.
            long linesNumber = 0;
            for (String key : keySet) {
                linesNumber += rootObject.get(key).getAsLong();
            }

            // Create empty HashMap.
            HashMap<String, String> languageDistributionMap = new HashMap<>();

            // Put the percentages of language distribution.
            for (String key : keySet) {
                final double percentage = rootObject.get(key).getAsLong() * 100.0 / linesNumber;
                final String percentageText = String.format("%.2f%%", percentage);
                languageDistributionMap.put(key, percentageText);
            }

            return languageDistributionMap;
        } catch (IOException e) {
            logger.error(e.getStackTrace().toString());
        }

        return null;
    }
}
