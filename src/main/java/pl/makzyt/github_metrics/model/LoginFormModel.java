package pl.makzyt.github_metrics.model;

import org.hibernate.validator.constraints.URL;
import pl.makzyt.github_metrics.validator.SonarCredentials;

@SonarCredentials
public class LoginFormModel {
    @URL(message = "{validation.invalidUrl}")
    private String host;

    private String login;

    private String password;

    public LoginFormModel() {
        host = "http://localhost:9000";
        login = "";
        password = "";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
