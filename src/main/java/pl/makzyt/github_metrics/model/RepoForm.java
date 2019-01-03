package pl.makzyt.github_metrics.model;

import javax.validation.constraints.NotEmpty;

public class RepoForm {
    @NotEmpty(message = "{validation.emptyData}")
    private String repoUrl;

    public RepoForm() {
        this.repoUrl = "";
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}
