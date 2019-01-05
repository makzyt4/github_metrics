package pl.makzyt.github_metrics.util.sonarqube;

import java.io.IOException;

public class SQProject {
    private SonarQube sonarQube;
    private String key;

    protected SQProject(SonarQube sonarQube, String key) {
        this.sonarQube = sonarQube;
        this.key = key;
    }

    public SQAnalysis getAnalysis() throws IOException {
        SQAnalysis analysis = new SQAnalysis(sonarQube, key);

        return analysis;
    }

    public String getKey() {
        return key;
    }
}
