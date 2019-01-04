package pl.makzyt.github_metrics.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RepositoryUrlValidator
        implements ConstraintValidator<RepositoryUrlConstraint, String> {

    @Override
    public void initialize(RepositoryUrlConstraint repoUrl) {
    }

    @Override
    public boolean isValid(String repoUrl, ConstraintValidatorContext ctx) {
        return false;
    }
}
