package com.absys.saas.tenant.platform.outbox.domain.model;

import java.util.UUID;

public record OutboxEventId(UUID value) {

    public OutboxEventId {
        if (value == null) {
            throw new IllegalArgumentException("Outbox event ID cannot be null");
        }
    }

    public static OutboxEventId generate() {
        return new OutboxEventId(UUID.randomUUID());
    }

    public static OutboxEventId of(UUID value) {
        return new OutboxEventId(value);
    }
}