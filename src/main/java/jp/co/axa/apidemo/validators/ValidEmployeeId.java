package jp.co.axa.apidemo.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmployeeIdValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmployeeId {

    /**
     * Retrieves the error message to be displayed when the employee ID is invalid.
     *
     * @return the error message.
     */
    String message() default "Invalid employee id";

    /**
     * Retrieves the validation groups for the constraint.
     *
     * @return the validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Retrieves the payload for the constraint.
     *
     * @return the payload.
     */
    Class<? extends Payload>[] payload() default {};
}
