package com.eventmanagement.dto;

import java.util.Set;

public class AuthResponseDTO {

    private String token;
    private String type = "Bearer";
    private UserResponseDTO user;
    private Set<String> roles;

    // Constructeurs
    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, UserResponseDTO user, Set<String> roles) {
        this.token = token;
        this.user = user;
        this.roles = roles;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}