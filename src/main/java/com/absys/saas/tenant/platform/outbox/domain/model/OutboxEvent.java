package com.absys.saas.tenant.platform.outbox.domain.model;

import java.time.Instant;

public class OutboxEvent {

    private static final int MAX_RETRIES = 5;

    private final OutboxEventId id;
    private final String eventType;
    private final String aggregateType;
    private final String aggregateId;
    private final String payload;
    private final Instant createdAt;
    private OutboxEventStatus status;
    private Instant processedAt;
    private int retryCount;
    private Instant nextAttemptAt;

    private OutboxEvent(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload, Instant createdAt, OutboxEventStatus status, Instant processedAt, int retryCount, Instant nextAttemptAt) {
        this.id = id;
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.createdAt = createdAt;
        this.status = status;
        this.processedAt = processedAt;
        this.retryCount = retryCount;
        this.nextAttemptAt = nextAttemptAt;
    }

    public static OutboxEvent create(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload) {
        return new OutboxEvent(id, eventType, aggregateType, aggregateId, payload, Instant.now(), OutboxEventStatus.PENDING, null, 0, Instant.now());
    }

    public static OutboxEvent restore(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload, Instant createdAt, OutboxEventStatus status, Instant processedAt, int retryCount, Instant nextAttemptAt) {
        return new OutboxEvent(id, eventType, aggregateType, aggregateId, payload, createdAt, status, processedAt, retryCount, nextAttemptAt);
    }

    public void markProcessed() {

        if (status == OutboxEventStatus.PROCESSED) {
            throw new IllegalStateException("Outbox event is already processed");
        }

        this.status = OutboxEventStatus.PROCESSED;
        this.processedAt = Instant.now();
    }

    public void markFailed() {

        if (status == OutboxEventStatus.PROCESSED) {
            throw new IllegalStateException("Processed event cannot fail");
        }

        this.status = OutboxEventStatus.FAILED;
    }

    public void registerFailure() {

        if (status != OutboxEventStatus.PENDING) {
            throw new IllegalStateException("Only pending events can be retried");
        }

        retryCount++;

        if (retryCount >= MAX_RETRIES) {
            markFailed();
            return;
        }

        nextAttemptAt = Instant.now().plusSeconds(calculateBackoffSeconds());
    }

    private long calculateBackoffSeconds() {

        return switch (retryCount) {
            case 1 -> 5;
            case 2 -> 30;
            case 3 -> 120;
            case 4 -> 600;
            default -> 0;
        };
    }

    public boolean isRetryable() {
        return status == OutboxEventStatus.PENDING && retryCount < MAX_RETRIES;
    }

    public OutboxEventId id() {
        return id;
    }

    public String eventType() {
        return eventType;
    }

    public String aggregateType() {
        return aggregateType;
    }

    public String aggregateId() {
        return aggregateId;
    }

    public String payload() {
        return payload;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public OutboxEventStatus status() {
        return status;
    }

    public Instant processedAt() {
        return processedAt;
    }

    public int retryCount() {
        return retryCount;
    }

    public Instant nextAttemptAt() {
        return nextAttemptAt;
    }
}