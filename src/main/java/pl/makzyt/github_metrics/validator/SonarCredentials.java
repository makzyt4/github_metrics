package pl.makzyt.github_metrics.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SonarCredentialsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SonarCredentials {
    String message() default "Invalid Sonar credentials";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
