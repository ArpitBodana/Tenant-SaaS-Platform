package com.absys.saas.tenant.platform.identity.infrastructure.security;

import java.util.UUID;

public final class TenantContext {

    private static final ThreadLocal<UUID> TENANT_ID = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setTenantId(UUID tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static UUID getTenantId() {
        return TENANT_ID.get();
    }

    public static UUID requireTenantId() {

        UUID tenantId = TENANT_ID.get();

        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is not available");
        }

        return tenantId;
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}