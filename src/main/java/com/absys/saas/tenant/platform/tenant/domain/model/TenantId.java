package com.absys.saas.tenant.platform.tenant.domain.model;

import java.util.UUID;

public record TenantId(UUID value) {

    public TenantId {
        if (value == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }
    }

    public static TenantId generate() {
        return new TenantId(UUID.randomUUID());
    }

    public static TenantId of(UUID value) {
        return new TenantId(value);
    }
}