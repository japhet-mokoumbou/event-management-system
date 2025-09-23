package com.eventmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {


    String message() default "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
