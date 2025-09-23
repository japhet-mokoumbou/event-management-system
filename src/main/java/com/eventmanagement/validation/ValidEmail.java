package com.eventmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "L'email doit être valide et unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
