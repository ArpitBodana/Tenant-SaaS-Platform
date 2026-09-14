package com.absys.saas.tenant.platform.identity.domain.model;

import java.util.UUID;

public class User {

    private final UserId id;
    private final UUID tenantId;
    private final UserEmail email;
    private String passwordHash;
    private UserRole role;
    private UserStatus status;

    private User(UserId id, UUID tenantId, UserEmail email, String passwordHash, UserRole role, UserStatus status) {
        this.id = id;
        this.tenantId = tenantId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
    }

    public static User create(UserId id, UUID tenantId, UserEmail email, String passwordHash, UserRole role) {
        return new User(id, tenantId, email, passwordHash, role, UserStatus.ACTIVE);
    }

    public static User restore(UserId id, UUID tenantId, UserEmail email, String passwordHash, UserRole role, UserStatus status) {
        return new User(id, tenantId, email, passwordHash, role, status);
    }

    public void activate() {
        if (status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }

        status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (status == UserStatus.INACTIVE) {
            throw new IllegalStateException("User is already inactive");
        }

        status = UserStatus.INACTIVE;
    }

    public UserId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public UserEmail email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public UserRole role() {
        return role;
    }

    public UserStatus status() {
        return status;
    }
}