package com.absys.saas.tenant.platform.outbox.infrastructure.persistence;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events", indexes = {@Index(name = "idx_outbox_unprocessed", columnList = "processed_at,created_at")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OutboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(name = "event_type", nullable = false, length = 255)
    private String eventType;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxEventStatus status;

    @Column(name = "next_attempt_at", nullable = false)
    private Instant nextAttemptAt;

    public void markProcessed(Instant processedAt) {
        this.status = OutboxEventStatus.PROCESSED;
        this.processedAt = processedAt;
    }

    public void registerFailure(OutboxEventStatus status, int retryCount, Instant nextAttemptAt) {
        this.status = status;
        this.retryCount = retryCount;
        this.nextAttemptAt = nextAttemptAt;
    }

}