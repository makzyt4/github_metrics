package pl.makzyt.github_metrics.validator;

import org.springframework.beans.factory.annotation.Autowired;
import pl.makzyt.github_metrics.model.LoginFormModel;
import pl.makzyt.github_metrics.util.sonarqube.SonarQube;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class SonarCredentialsValidator
        implements ConstraintValidator<SonarCredentials, LoginFormModel> {

    @Autowired
    private SonarQube sonarQube;

    @Override
    public boolean isValid(LoginFormModel value,
                           ConstraintValidatorContext context) {

        if (!value.getHost().isEmpty()) {
            sonarQube.setHost(value.getHost());
        }

        sonarQube.setCredentials(value.getLogin(), value.getPassword());

        try {
            sonarQube.login();
            return sonarQube.privileged();
        } catch (IOException e) {
            return false;
        }
    }
}
