package com.absys.saas.tenant.platform.tenant.domain.model;

public record TenantName(String value) {

    public TenantName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant name cannot be empty");
        }

        if (value.length() > 150) {
            throw new IllegalArgumentException(
                    "Tenant name cannot exceed 150 characters"
            );
        }

        value = value.trim();
    }
}