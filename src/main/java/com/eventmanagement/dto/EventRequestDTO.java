package com.eventmanagement.dto;

import com.eventmanagement.validation.ValidEventDates;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@ValidEventDates
public class EventRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 180, message = "Le titre doit contenir entre 3 et 180 caractères")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 4000, message = "La description doit contenir entre 10 et 4000 caractères")
    private String description;

    @NotBlank(message = "La ville est obligatoire")
    @Size(min = 2, max = 120, message = "La ville doit contenir entre 2 et 120 caractères")
    private String City;

    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime startsAt;

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDateTime endsAt;

    public EventRequestDTO() {
    }

    public EventRequestDTO(String title, String description, String city, LocalDateTime startsAt, LocalDateTime endsAt) {
        this.title = title;
        this.description = description;
        City = city;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }
}
