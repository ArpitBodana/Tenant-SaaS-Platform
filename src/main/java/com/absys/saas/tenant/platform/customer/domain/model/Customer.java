package com.absys.saas.tenant.platform.customer.domain.model;

import java.util.UUID;

public class Customer {

    private final CustomerId id;
    private final UUID tenantId;

    private CustomerName name;
    private CustomerEmail email;
    private String phone;
    private CustomerStatus status;

    private Customer(CustomerId id, UUID tenantId, CustomerName name, CustomerEmail email, String phone, CustomerStatus status) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public static Customer create(CustomerId id, UUID tenantId, CustomerName name, CustomerEmail email, String phone) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        return new Customer(id, tenantId, name, email, normalizePhone(phone), CustomerStatus.ACTIVE);
    }

    public static Customer restore(CustomerId id, UUID tenantId, CustomerName name, CustomerEmail email, String phone, CustomerStatus status) {

        return new Customer(id, tenantId, name, email, phone, status);
    }

    public void update(CustomerName name, CustomerEmail email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = normalizePhone(phone);
    }

    public void activate() {

        if (status == CustomerStatus.ACTIVE) {
            throw new IllegalStateException("Customer is already active");
        }

        status = CustomerStatus.ACTIVE;
    }

    public void deactivate() {

        if (status == CustomerStatus.INACTIVE) {
            throw new IllegalStateException("Customer is already inactive");
        }

        status = CustomerStatus.INACTIVE;
    }

    private static String normalizePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return null;
        }

        String normalized = phone.trim();

        if (normalized.length() > 30) {
            throw new IllegalArgumentException("Phone number cannot exceed 30 characters");
        }

        return normalized;
    }

    public CustomerId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public CustomerName name() {
        return name;
    }

    public CustomerEmail email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public CustomerStatus status() {
        return status;
    }
}