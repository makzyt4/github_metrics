package pl.makzyt.github_metrics.model;

import pl.makzyt.github_metrics.util.RepositoryInfoExtractor;

import java.util.HashMap;

public class RepositoryInfo {
    private final HashMap<String, String> languageDistribution;

    public RepositoryInfo(RepositoryForm repositoryForm) {
        RepositoryInfoExtractor extractor =
                new RepositoryInfoExtractor(repositoryForm.getRepositoryUrl());

        languageDistribution = extractor.getLanguageDistribution();
    }
}
