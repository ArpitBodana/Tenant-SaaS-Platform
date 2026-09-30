package com.absys.saas.tenant.platform.audit.domain.model;

import java.time.Instant;
import java.util.UUID;

public class AuditEvent {

    private final AuditEventId id;
    private final UUID tenantId;
    private final UUID actorId;
    private final AuditActorType actorType;
    private final AuditAction action;
    private final String aggregateType;
    private final String aggregateId;
    private final String description;
    private final Instant occurredAt;

    private AuditEvent(
            AuditEventId id,
            UUID tenantId,
            UUID actorId,
            AuditActorType actorType,
            AuditAction action,
            String aggregateType,
            String aggregateId,
            String description,
            Instant occurredAt) {

        this.id = id;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.actorType = actorType;
        this.action = action;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.description = description;
        this.occurredAt = occurredAt;
    }

    public static AuditEvent create(
            AuditEventId id,
            UUID tenantId,
            UUID actorId,
            AuditActorType actorType,
            AuditAction action,
            String aggregateType,
            String aggregateId,
            String description) {

        if (id == null) {
            throw new IllegalArgumentException("Audit event ID cannot be null");
        }

        if (actorType == null) {
            throw new IllegalArgumentException("Actor type cannot be null");
        }

        if (action == null) {
            throw new IllegalArgumentException("Audit action cannot be null");
        }

        if (aggregateType == null || aggregateType.isBlank()) {
            throw new IllegalArgumentException(
                    "Aggregate type cannot be blank"
            );
        }

        if (aggregateId == null || aggregateId.isBlank()) {
            throw new IllegalArgumentException(
                    "Aggregate ID cannot be blank"
            );
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Audit description cannot be blank"
            );
        }

        if (actorType == AuditActorType.USER && actorId == null) {
            throw new IllegalArgumentException(
                    "User actor ID cannot be null for USER actor type"
            );
        }

        return new AuditEvent(
                id,
                tenantId,
                actorId,
                actorType,
                action,
                aggregateType,
                aggregateId,
                description,
                Instant.now()
        );
    }

    public AuditEventId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public UUID actorId() {
        return actorId;
    }

    public AuditActorType actorType() {
        return actorType;
    }

    public AuditAction action() {
        return action;
    }

    public String aggregateType() {
        return aggregateType;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public String description() {
        return description;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public static AuditEvent restore(
            AuditEventId id,
            UUID tenantId,
            UUID actorId,
            AuditActorType actorType,
            AuditAction action,
            String aggregateType,
            String aggregateId,
            String description,
            Instant occurredAt) {

        return new AuditEvent(
                id,
                tenantId,
                actorId,
                actorType,
                action,
                aggregateType,
                aggregateId,
                description,
                occurredAt
        );
    }
}