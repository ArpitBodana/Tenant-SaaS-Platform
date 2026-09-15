package com.absys.saas.tenant.platform.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldCalculateSubtotal() {

        OrderItem item = OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 3, new BigDecimal("250.00"));

        assertEquals(new BigDecimal("750.00"), item.subtotal());
    }

    @Test
    void shouldRejectZeroQuantity() {

        assertThrows(IllegalArgumentException.class, () -> OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 0, new BigDecimal("100.00")));
    }

    @Test
    void shouldRejectNegativeQuantity() {

        assertThrows(IllegalArgumentException.class, () -> OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), -1, new BigDecimal("100.00")));
    }

    @Test
    void shouldRejectNegativePrice() {

        assertThrows(IllegalArgumentException.class, () -> OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 1, new BigDecimal("-10.00")));
    }

    @Test
    void shouldChangeQuantity() {

        OrderItem item = OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 2, new BigDecimal("500.00"));

        item.changeQuantity(5);

        assertEquals(5, item.quantity());

        assertEquals(new BigDecimal("2500.00"), item.subtotal());
    }
}