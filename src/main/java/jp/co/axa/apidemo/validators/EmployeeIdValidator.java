package jp.co.axa.apidemo.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmployeeIdValidator implements ConstraintValidator<ValidEmployeeId, Long> {
    /**
     * Initializes the validator.
     *
     * @param constraintAnnotation the constraint annotation.
     */
    @Override
    public void initialize(ValidEmployeeId constraintAnnotation) {}


    /**
     * Checks if the employee ID is valid.
     *
     * @param employeeId the employee ID to validate.
     * @param context    the validation context.
     * @return true if the employee ID is valid, false otherwise.
     */
    @Override
    public boolean isValid(Long employeeId, ConstraintValidatorContext context) {
        return employeeId != null && employeeId > 0;
    }
}
