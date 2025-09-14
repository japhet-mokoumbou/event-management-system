package com.eventmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDTO {

    @NotBlank(message = "Le nom complet est obligatoire")
    @Size(min = 2, max = 120, message = "Le nom doit contenir entre 2 et 120 caractères")
    private String fullname;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Size(max = 180, message = "L'email ne peut pas dépasser 180 caractères")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String fullname, String email, String password) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
