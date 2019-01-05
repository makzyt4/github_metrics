package pl.makzyt.github_metrics.util;

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

import java.io.IOException;

public class SonarQube {
    private String host;
    private String username;
    private String password;
    private HttpClient client;

    public SonarQube() {
        host = "http://localhost:9000";
        username = "admin";
        password = "admin";
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

        final String urlLogin = host +
                String.format("/api/authentication/login?login=%s&password=%s",
                        username, password);

        System.out.println(urlLogin);

        HttpPost httpPost = new HttpPost(urlLogin);
        client.execute(httpPost);
    }

    public void logout() throws IOException {
        final String urlLogout = host + "/api/authentication/logout";

        HttpPost httpPost = new HttpPost(urlLogout);
        client.execute(httpPost);
    }

    public boolean authenticated() throws IOException {
        final String urlValidate = host + "/api/projects/search";
        HttpGet httpGet = new HttpGet(urlValidate);
        HttpResponse response = client.execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());
        JsonParser jsonParser = new JsonParser();
        JsonObject rootObj = jsonParser.parse(jsonString).getAsJsonObject();

        return rootObj.get("errors") == null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
