package com.eventmanagement.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TicketTypeRequestDTO {

    @NotBlank(message = "Le nom du type de billet est obligatoire")
    @Size(min = 2, max = 120, message = "Le nom doit contenir entre 2 et 120 caractères")
    private String name;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif ou nul")
    private BigDecimal price;

    @NotNull(message = "Le stock est obligatoire")
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;

    public TicketTypeRequestDTO() {
    }

    public TicketTypeRequestDTO(String name, BigDecimal price, Integer stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
