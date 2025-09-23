package com.eventmanagement.validation;

import com.eventmanagement.dto.EventRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class EventDatesValidator implements ConstraintValidator<ValidEventDates, EventRequestDTO> {
    @Override
    public void initialize(ValidEventDates constraintAnnotation) {

    }

    @Override
    public boolean isValid(EventRequestDTO eventRequestDTO, ConstraintValidatorContext context) {
        if(eventRequestDTO == null){
            return true; // la validation est gérée par @NotNull
        }

        LocalDateTime startsAt = eventRequestDTO.getStartsAt();
        LocalDateTime endsAt = eventRequestDTO.getEndsAt();

        if(startsAt == null || endsAt == null){
            return true;
        }

        if (endsAt.isBefore(startsAt) || endsAt.isEqual(startsAt)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "La date de fin doit être posterieure à la date de début"
            ).addConstraintViolation();
            return false;
        }

        // Vérifier que l'événement n'est pas dans le passé
        if (startsAt.isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "La date de début ne peut pas être dans le passé"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

}
