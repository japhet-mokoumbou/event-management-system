package com.eventmanagement.validation;

import com.eventmanagement.dto.ReservationRequestDTO;
import com.eventmanagement.entity.TicketType;
import com.eventmanagement.repository.TicketTypeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator implements ConstraintValidator<ValidReservation, ReservationRequestDTO> {

    private final TicketTypeRepository ticketTypeRepository;

    public ReservationValidator(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public void initialize(ValidReservation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationRequestDTO reservation, ConstraintValidatorContext context) {

        if(reservation == null || reservation.getItems() == null){
            return true;
        }

        boolean isValid = true;

        if(reservation.getItems().isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "La réservation doit contenir au moins un ticket"
            ).addConstraintViolation();
            isValid = false;
        }

        for(var item: reservation.getItems()){
            TicketType ticketType = ticketTypeRepository.findById(item.getTicketTypeId())
                    .orElse(null);

            if (ticketType == null){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Type de ticket introuvable avec l'ID: " + item.getTicketTypeId()
                ).addConstraintViolation();
                isValid = false;
                continue;
            }

            if (ticketType.getStock() < item.getQuantity()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "Stock insuffisant pour le ticket '" + ticketType.getName() +
                                "'. Disponible: " + ticketType.getStock() + ", Demandé: " + item.getQuantity()
                ).addConstraintViolation();
                isValid = false;
            }
        }
        return isValid;
    }
}
