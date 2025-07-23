package com.example.backend.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailConstraintValidator implements ConstraintValidator<ValidEmail, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) return false;
        boolean matches = email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        boolean allowedDomain = email.endsWith("@gmail.com");

        return matches && allowedDomain;
    }
}
