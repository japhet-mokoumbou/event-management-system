package com.eventmanagement.dto;

import java.time.Instant;
import java.util.Set;

public class UserResponseDTO {

    private Long id;
    private String fullname;
    private String email;
    private Set<String> roles;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String fullname, String email, Set<String> roles, boolean enabled, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
