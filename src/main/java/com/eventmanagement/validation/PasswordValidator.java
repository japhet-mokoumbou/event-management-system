package com.eventmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {


    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);


    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null ){
            return true;
        }
        if(!pattern.matcher(password).matches()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
