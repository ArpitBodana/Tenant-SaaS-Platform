package com.absys.saas.tenant.platform.product.domain.model;

public record ProductSku(String value) {

    public ProductSku {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product SKU cannot be empty");
        }

        value = value.trim().toUpperCase();

        if (value.length() > 100) {
            throw new IllegalArgumentException("Product SKU cannot exceed 100 characters");
        }

        if (!value.matches("^[A-Z0-9][A-Z0-9_-]*$")) {
            throw new IllegalArgumentException("Invalid product SKU");
        }
    }
}