package com.eventmanagement.dto;

import java.util.Set;

public class AuthResponseDTO {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String fullname;
    private String email;
    private Set<String> roles;

    // Constructeurs
    public AuthResponseDTO() {}

    public AuthResponseDTO(String accessToken, String tokenType, Long userId, String fullname, String email, Set<String> roles) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.fullname = fullname;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}