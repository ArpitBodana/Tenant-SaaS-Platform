package com.absys.saas.tenant.platform.audit.domain.model;

import java.util.UUID;

public record AuditEventId(UUID value) {

    public AuditEventId {
        if (value == null) {
            throw new IllegalArgumentException("Audit event ID cannot be null");
        }
    }

    public static AuditEventId generate() {
        return new AuditEventId(UUID.randomUUID());
    }

    public static AuditEventId of(UUID value) {
        return new AuditEventId(value);
    }
}