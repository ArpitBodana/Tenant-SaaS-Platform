package com.absys.saas.tenant.platform.customer.domain.model;

public record CustomerEmail(String value) {

    public CustomerEmail {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be empty");
        }

        value = value.trim().toLowerCase();

        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid customer email");
        }
    }
}