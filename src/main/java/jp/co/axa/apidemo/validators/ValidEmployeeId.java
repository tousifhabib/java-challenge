package jp.co.axa.apidemo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmployeeIdValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmployeeId {

    String message() default "Invalid employee id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
