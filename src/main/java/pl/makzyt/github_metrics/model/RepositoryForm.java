package pl.makzyt.github_metrics.model;

import org.hibernate.validator.constraints.URL;
import pl.makzyt.github_metrics.validator.RepositoryUrlConstraint;

import javax.validation.constraints.NotEmpty;

public class RepositoryForm {
    @NotEmpty(message = "{validation.emptyData}")
    @URL(message = "{validation.invalidUrl}")
    @RepositoryUrlConstraint(message = "{validation.invalidRepositoryUrl}")
    private String repositoryUrl;

    public RepositoryForm() {
        this.repositoryUrl = "";
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repoUrl) {
        this.repositoryUrl = repoUrl;
    }
}
