package pl.makzyt.github_metrics.util.sonarqube;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class SonarQube {
    private String host;
    private String username;
    private String password;
    private HttpClient client;

    public SonarQube() throws IOException {
        host = "http://localhost:9000";
        username = "admin";
        password = "admin";
        client = HttpClientBuilder.create().build();
    }

    public void login() throws IOException {
        login(username, password);
    }

    public void login(String username, String password) throws IOException {
        this.username = username;
        this.password = password;

        Credentials credentials = new UsernamePasswordCredentials(
                username,
                password
        );
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(provider)
                .build();

        String urlLogin = host +
                String.format("/api/authentication/login?login=%s&password=%s",
                        username, password);

        HttpPost httpPost = new HttpPost(urlLogin);
        client.execute(httpPost);
    }

    public void logout() throws IOException {
        String urlLogout = host + "/api/authentication/logout";

        HttpPost httpPost = new HttpPost(urlLogout);
        client.execute(httpPost);
    }

    public boolean privileged() throws IOException {
        String urlToValidate = host + "/api/projects/search";
        HttpGet httpGet = new HttpGet(urlToValidate);
        HttpResponse response = client.execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser jsonParser = new JsonParser();
        JsonObject rootObj = jsonParser.parse(jsonString).getAsJsonObject();

        boolean privilegeError = rootObj.get("errors") != null;

        return !privilegeError;
    }

    public HashMap<String, SQProject> getProjects() throws IOException {
        String urlToValidate = host + "/api/projects/search";
        HttpGet httpGet = new HttpGet(urlToValidate);
        HttpResponse response = client.execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser jsonParser = new JsonParser();
        JsonObject rootObj = jsonParser.parse(jsonString).getAsJsonObject();

        boolean privilegeError = rootObj.get("errors") != null;

        if (privilegeError) {
            return null;
        }

        HashMap<String, SQProject> projects = new HashMap<>();

        JsonArray components = rootObj.get("components").getAsJsonArray();

        for (JsonElement element : components) {
            String key = element.getAsJsonObject().get("key").getAsString();
            projects.put(key, new SQProject(this, key));
        }

        return projects;
    }

    public String getHost() {
        return host;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected HttpClient getClient() {
        return client;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
