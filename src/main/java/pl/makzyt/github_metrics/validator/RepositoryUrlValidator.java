package pl.makzyt.github_metrics.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RepositoryUrlValidator
        implements ConstraintValidator<RepositoryUrlConstraint, String> {
    private final Logger logger = LoggerFactory.getLogger(RepositoryUrlValidator.class);

    @Override
    public void initialize(RepositoryUrlConstraint repoUrl) {
    }

    @Override
    public boolean isValid(String repositoryUrl, ConstraintValidatorContext ctx) {
        final String githubUrlPattern = "https?://(www\\.)?github\\.com/[a-zA-Z0-9]+/[a-zA-Z0-9]+/?";
        final boolean isGithubUrl = repositoryUrl.matches(githubUrlPattern);

        URL url;
        try {
            url = new URL(repositoryUrl);
        } catch (MalformedURLException e) {
            logger.warn("Malformed URL found, given URL is invalid.");
            logger.error(e.getStackTrace().toString());

            return false;
        }

        // Check if response code is 200 (OK)
        try {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            final int code = connection.getResponseCode();

            return code == 200 && isGithubUrl;
        } catch (IOException e) {
            logger.warn("IOException, given URL is invalid.");
            logger.error(e.getStackTrace().toString());

            e.printStackTrace();
        }

        return false;
    }
}
