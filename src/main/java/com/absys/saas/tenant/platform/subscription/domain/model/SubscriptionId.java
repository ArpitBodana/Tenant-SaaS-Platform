package com.absys.saas.tenant.platform.subscription.domain.model;

import java.util.UUID;

public record SubscriptionId(UUID value) {

    public SubscriptionId {
        if (value == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null");
        }
    }

    public static SubscriptionId generate() {
        return new SubscriptionId(UUID.randomUUID());
    }

    public static SubscriptionId of(UUID value) {
        return new SubscriptionId(value);
    }
}