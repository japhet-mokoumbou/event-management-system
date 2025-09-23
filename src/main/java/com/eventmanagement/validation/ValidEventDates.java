package com.eventmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EventDatesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDates {

    String message() default "La date de fin doit être postérieure à la date de début";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
