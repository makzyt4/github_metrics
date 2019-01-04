package pl.makzyt.github_metrics.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RepositoryUrlValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryUrlConstraint {
    String message() default "Invalid repo URL";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
