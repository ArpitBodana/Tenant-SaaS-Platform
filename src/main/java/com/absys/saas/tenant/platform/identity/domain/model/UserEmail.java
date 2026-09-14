package com.absys.saas.tenant.platform.identity.domain.model;

public record UserEmail(String value) {

    public UserEmail {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        value = value.trim().toLowerCase();

        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
}