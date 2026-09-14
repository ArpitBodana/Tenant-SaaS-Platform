package com.absys.saas.tenant.platform.order.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {

    private final OrderItemId id;
    private final UUID productId;

    private int quantity;
    private BigDecimal unitPrice;

    private OrderItem(OrderItemId id, UUID productId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItem create(OrderItemId id, UUID productId, int quantity, BigDecimal unitPrice) {

        validate(productId, quantity, unitPrice);

        return new OrderItem(id, productId, quantity, unitPrice);
    }

    public static OrderItem restore(OrderItemId id, UUID productId, int quantity, BigDecimal unitPrice) {

        validate(productId, quantity, unitPrice);

        return new OrderItem(id, productId, quantity, unitPrice);
    }

    public void changeQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        this.quantity = quantity;
    }

    public BigDecimal subtotal() {

        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private static void validate(UUID productId, int quantity, BigDecimal unitPrice) {

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (unitPrice == null) {
            throw new IllegalArgumentException("Unit price cannot be null");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
    }

    public OrderItemId id() {
        return id;
    }

    public UUID productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }
}