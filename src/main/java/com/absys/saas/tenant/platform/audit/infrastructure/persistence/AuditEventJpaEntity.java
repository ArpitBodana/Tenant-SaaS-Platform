package com.absys.saas.tenant.platform.audit.infrastructure.persistence;

import com.absys.saas.tenant.platform.audit.domain.model.AuditAction;
import com.absys.saas.tenant.platform.audit.domain.model.AuditActorType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_events",
        indexes = {
                @Index(
                        name = "idx_audit_events_tenant_occurred",
                        columnList = "tenant_id,occurred_at"
                ),
                @Index(
                        name = "idx_audit_events_aggregate",
                        columnList = "aggregate_type,aggregate_id"
                )
        }
)
public class AuditEventJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "actor_id")
    private UUID actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", nullable = false, length = 20)
    private AuditActorType actorType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuditAction action;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected AuditEventJpaEntity() {
    }

    public AuditEventJpaEntity(
            UUID id,
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

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getActorId() {
        return actorId;
    }

    public AuditActorType getActorType() {
        return actorType;
    }

    public AuditAction getAction() {
        return action;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}