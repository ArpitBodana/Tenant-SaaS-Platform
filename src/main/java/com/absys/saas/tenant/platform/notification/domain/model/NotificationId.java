package com.absys.saas.tenant.platform.notification.domain.model;

import java.util.UUID;

public record NotificationId(UUID value) {

    public NotificationId {
        if (value == null) {
            throw new IllegalArgumentException("Notification ID cannot be null");
        }
    }

    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }

    public static NotificationId of(UUID value) {
        return new NotificationId(value);
    }
}