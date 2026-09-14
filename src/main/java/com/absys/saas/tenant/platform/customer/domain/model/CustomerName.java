package com.absys.saas.tenant.platform.customer.domain.model;

public record CustomerName(String value) {

    public CustomerName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }

        value = value.trim();

        if (value.length() > 150) {
            throw new IllegalArgumentException(
                    "Customer name cannot exceed 150 characters"
            );
        }
    }
}