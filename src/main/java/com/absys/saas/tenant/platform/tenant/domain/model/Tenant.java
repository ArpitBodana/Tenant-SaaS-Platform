package com.absys.saas.tenant.platform.tenant.domain.model;

public class Tenant {

    private final TenantId id;
    private TenantName name;
    private TenantStatus status;

    private Tenant(TenantId id, TenantName name, TenantStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static Tenant create(TenantId id, TenantName name) {
        return new Tenant(id, name, TenantStatus.ACTIVE);
    }

    public void activate() {
        if (status == TenantStatus.ACTIVE) {
            throw new IllegalStateException("Tenant is already active");
        }

        status = TenantStatus.ACTIVE;
    }

    public void suspend() {
        if (status == TenantStatus.SUSPENDED) {
            throw new IllegalStateException("Tenant is already suspended");
        }

        status = TenantStatus.SUSPENDED;
    }

    public void rename(TenantName newName) {
        this.name = newName;
    }

    public TenantId id() {
        return id;
    }

    public TenantName name() {
        return name;
    }

    public TenantStatus status() {
        return status;
    }
}