package pl.makzyt.github_metrics.model;

import pl.makzyt.github_metrics.util.RepositoryInfoExtractor;

import java.util.HashMap;

public class RepositoryInfo {
    private final String userName;
    private final String repositoryName;
    private final HashMap<String, Double> languageDistribution;

    public RepositoryInfo(RepositoryForm repositoryForm) {
        RepositoryInfoExtractor extractor =
                new RepositoryInfoExtractor(repositoryForm.getRepositoryUrl());

        userName = extractor.getUserName();
        repositoryName = extractor.getRepositoryName();
        languageDistribution = extractor.getLanguageDistribution();
    }

    public String getUserName() {
        return userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public HashMap<String, Double> getLanguageDistribution() {
        return languageDistribution;
    }
}
