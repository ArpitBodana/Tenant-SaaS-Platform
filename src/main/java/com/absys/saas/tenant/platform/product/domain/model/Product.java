package com.absys.saas.tenant.platform.product.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

    private final ProductId id;
    private final UUID tenantId;

    private ProductName name;
    private ProductSku sku;
    private BigDecimal price;
    private ProductStatus status;

    private Product(ProductId id, UUID tenantId, ProductName name, ProductSku sku, BigDecimal price, ProductStatus status) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.status = status;
    }

    /**
     * Creates a NEW product.
     */
    public static Product create(ProductId id, UUID tenantId, ProductName name, ProductSku sku, BigDecimal price) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        validatePrice(price);

        return new Product(id, tenantId, name, sku, price, ProductStatus.ACTIVE);
    }

    /**
     * Reconstructs an EXISTING product from persistence.
     */
    public static Product restore(ProductId id, UUID tenantId, ProductName name, ProductSku sku, BigDecimal price, ProductStatus status) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        validatePrice(price);

        if (status == null) {
            throw new IllegalArgumentException("Product status cannot be null");
        }

        return new Product(id, tenantId, name, sku, price, status);
    }

    public void update(ProductName name, ProductSku sku, BigDecimal price) {

        validatePrice(price);

        this.name = name;
        this.sku = sku;
        this.price = price;
    }

    public void activate() {

        if (status == ProductStatus.ACTIVE) {
            throw new IllegalStateException("Product is already active");
        }

        status = ProductStatus.ACTIVE;
    }

    public void deactivate() {

        if (status == ProductStatus.INACTIVE) {
            throw new IllegalStateException("Product is already inactive");
        }

        status = ProductStatus.INACTIVE;
    }

    private static void validatePrice(BigDecimal price) {

        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
    }

    public ProductId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public ProductName name() {
        return name;
    }

    public ProductSku sku() {
        return sku;
    }

    public BigDecimal price() {
        return price;
    }

    public ProductStatus status() {
        return status;
    }
}