package com.absys.saas.tenant.platform.order.domain.model;

import java.math.BigDecimal;
import java.util.*;

public class Order {

    private final OrderId id;
    private final UUID tenantId;
    private final UUID customerId;

    private OrderStatus status;

    private final List<OrderItem> items;

    private Order(OrderId id, UUID tenantId, UUID customerId, OrderStatus status, List<OrderItem> items) {
        this.id = id;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.status = status;
        this.items = new ArrayList<>(items);
    }

    public static Order create(OrderId id, UUID tenantId, UUID customerId) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        return new Order(id, tenantId, customerId, OrderStatus.DRAFT, List.of());
    }

    public static Order restore(OrderId id, UUID tenantId, UUID customerId, OrderStatus status, List<OrderItem> items) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }

        return new Order(id, tenantId, customerId, status, items);
    }

    public void addItem(OrderItem item) {

        ensureDraft();

        Objects.requireNonNull(item, "Order item cannot be null");

        boolean productAlreadyAdded = items.stream().anyMatch(existing -> existing.productId().equals(item.productId()));

        if (productAlreadyAdded) {
            throw new IllegalStateException("Product is already added to this order");
        }

        items.add(item);
    }

    public void removeItem(OrderItemId itemId) {

        ensureDraft();

        boolean removed = items.removeIf(item -> item.id().equals(itemId));

        if (!removed) {
            throw new IllegalArgumentException("Order item not found");
        }
    }

    public void confirm() {

        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Only draft orders can be confirmed");
        }

        if (items.isEmpty()) {
            throw new IllegalStateException("Order must contain at least one item");
        }

        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {

        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        status = OrderStatus.CANCELLED;
    }

    public BigDecimal totalAmount() {

        return items.stream().map(OrderItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void ensureDraft() {

        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Order can only be modified while in DRAFT status");
        }
    }

    public OrderId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public UUID customerId() {
        return customerId;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }
}