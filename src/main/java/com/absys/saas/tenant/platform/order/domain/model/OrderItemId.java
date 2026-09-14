package com.absys.saas.tenant.platform.order.domain.model;

import java.util.UUID;

public record OrderItemId(UUID value) {

    public OrderItemId {
        if (value == null) {
            throw new IllegalArgumentException("Order item ID cannot be null");
        }
    }

    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }

    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }
}