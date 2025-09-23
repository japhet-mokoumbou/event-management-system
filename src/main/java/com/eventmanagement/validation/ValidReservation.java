package com.eventmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReservationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservation {

    String message() default "La réservation n'est pas valide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
