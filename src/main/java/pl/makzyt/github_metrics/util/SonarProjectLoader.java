package pl.makzyt.github_metrics.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

public class SonarProjectLoader {
    private final Logger logger = LoggerFactory.getLogger(SonarProjectLoader.class);

    private final Properties properties = new Properties();
    private String baseUrl;
    private HttpClient client;
    private String projectKey = null;

    public SonarProjectLoader() {
        InputStream input = null;

        try {
            input = new FileInputStream("src/main/resources/sonar.properties");

            loadProperties(input);
            loadBaseUrl();
            loadHttpClient();

            loginWithCredentials();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private void loadProperties(InputStream input) throws IOException {
        properties.load(input);
    }

    private void loadHttpClient() {
        // Set credentials
        Credentials credentials = new UsernamePasswordCredentials(
                properties.getProperty("sonar.login"),
                properties.getProperty("sonar.password")
        );
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, credentials);

        // Build HTTP client
        client = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(provider)
                .build();
    }

    private void loadBaseUrl() {
        // Set base URL (eg. http://localhost:9000/)
        baseUrl = String.format("http://%s:%s/",
                properties.getProperty("sonar.host"),
                properties.getProperty("sonar.port"));

    }

    private void loginWithCredentials() throws IOException {
        final String urlLogin = baseUrl +
                String.format("api/authentication/login?login=%s&password=%s",
                        properties.getProperty("sonar.login"),
                        properties.getProperty("sonar.password"));

        HttpPost httpPost = new HttpPost(urlLogin);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", properties.getProperty("sonar.login")));
        params.add(new BasicNameValuePair("password", properties.getProperty("sonar.password")));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = client.execute(httpPost);
        String jsonString = EntityUtils.toString(response.getEntity());

        JsonParser parser = new JsonParser();
        JsonElement rootObj = parser.parse(jsonString);

        if (rootObj != null) {
            throw new IOException("Wrong credentials");
        }
    }

    public void loadProject(String projectKey) throws InvalidKeyException,
            IOException {
        // Login with given credentials

        final String urlProjects = baseUrl + "api/projects/search";

        // Get the projects
        HttpResponse response = client.execute(new HttpGet(urlProjects));
        String jsonString = EntityUtils.toString(response.getEntity());

        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(jsonString).getAsJsonObject();
        JsonElement errorObj = rootObj.get("error");

        if (errorObj != null) {
            this.projectKey = null;
            throw new IOException("Wrong credentials");
        }

        JsonArray components = rootObj.get("components").getAsJsonArray();

        // Iterate projects
        HashSet<String> projectKeys = new HashSet<>();
        for (JsonElement element : components) {
            String key = element.getAsJsonObject().get("key").getAsString();
            projectKeys.add(key);
        }

        if (!projectKeys.contains(projectKey)) {
            this.projectKey = null;
            throw new InvalidKeyException("Given project key doesn't exist!");
        }

        this.projectKey = projectKey;
    }
    public boolean loaded() {
        return this.projectKey != null;
    }
}
