package jp.co.axa.apidemo.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmployeeIdValidator implements ConstraintValidator<ValidEmployeeId, Long> {

    @Override
    public void initialize(ValidEmployeeId constraintAnnotation) {}

    @Override
    public boolean isValid(Long employeeId, ConstraintValidatorContext context) {
        // Here you can add your validation logic, e.g. check if id is not null and positive
        return employeeId != null && employeeId > 0;
    }
}
