package com.absys.saas.tenant.platform.product.domain.model;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        value = value.trim();

        if (value.length() > 150) {
            throw new IllegalArgumentException(
                    "Product name cannot exceed 150 characters"
            );
        }
    }
}