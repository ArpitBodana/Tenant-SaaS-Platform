package com.absys.saas.tenant.platform.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateDraftOrder() {

        UUID tenantId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Order order = Order.create(OrderId.generate(), tenantId, customerId);

        assertEquals(OrderStatus.DRAFT, order.status());
        assertEquals(tenantId, order.tenantId());
        assertEquals(customerId, order.customerId());
        assertTrue(order.items().isEmpty());
        assertEquals(BigDecimal.ZERO, order.totalAmount());
    }

    @Test
    void shouldAddOrderItem() {

        Order order = createOrder();

        OrderItem item = OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 2, new BigDecimal("500.00"));

        order.addItem(item);

        assertEquals(1, order.items().size());
        assertEquals(new BigDecimal("1000.00"), order.totalAmount());
    }

    @Test
    void shouldRejectDuplicateProduct() {

        Order order = createOrder();

        UUID productId = UUID.randomUUID();

        order.addItem(OrderItem.create(OrderItemId.generate(), productId, 2, new BigDecimal("500.00")));

        assertThrows(IllegalStateException.class, () -> order.addItem(OrderItem.create(OrderItemId.generate(), productId, 3, new BigDecimal("500.00"))));
    }

    @Test
    void shouldNotConfirmEmptyOrder() {

        Order order = createOrder();

        IllegalStateException exception = assertThrows(IllegalStateException.class, order::confirm);

        assertEquals("Order must contain at least one item", exception.getMessage());

        assertEquals(OrderStatus.DRAFT, order.status());
    }

    @Test
    void shouldConfirmOrderWithItems() {

        Order order = createOrder();

        order.addItem(OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 2, new BigDecimal("500.00")));

        order.confirm();

        assertEquals(OrderStatus.CONFIRMED, order.status());
    }

    @Test
    void shouldNotModifyConfirmedOrder() {

        Order order = createOrder();

        UUID productId = UUID.randomUUID();

        order.addItem(OrderItem.create(OrderItemId.generate(), productId, 2, new BigDecimal("500.00")));

        order.confirm();

        assertThrows(IllegalStateException.class, () -> order.addItem(OrderItem.create(OrderItemId.generate(), UUID.randomUUID(), 1, new BigDecimal("100.00"))));
    }

    @Test
    void shouldCancelOrder() {

        Order order = createOrder();

        order.cancel();

        assertEquals(OrderStatus.CANCELLED, order.status());
    }

    @Test
    void shouldNotCancelAlreadyCancelledOrder() {

        Order order = createOrder();

        order.cancel();

        assertThrows(IllegalStateException.class, order::cancel);
    }

    private Order createOrder() {

        return Order.create(OrderId.generate(), UUID.randomUUID(), UUID.randomUUID());
    }
}