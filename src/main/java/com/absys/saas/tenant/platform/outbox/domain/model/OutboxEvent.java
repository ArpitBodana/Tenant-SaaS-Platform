package com.absys.saas.tenant.platform.outbox.domain.model;

import java.time.Instant;

public class OutboxEvent {

    private final OutboxEventId id;
    private final String eventType;
    private final String aggregateType;
    private final String aggregateId;
    private final String payload;
    private final Instant createdAt;

    private Instant processedAt;
    private int retryCount;

    private OutboxEvent(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload, Instant createdAt, Instant processedAt, int retryCount) {
        this.id = id;
        this.eventType = eventType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.retryCount = retryCount;
    }

    public static OutboxEvent create(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload) {

        validate(eventType, aggregateType, aggregateId, payload);

        return new OutboxEvent(id, eventType, aggregateType, aggregateId, payload, Instant.now(), null, 0);
    }

    public static OutboxEvent restore(OutboxEventId id, String eventType, String aggregateType, String aggregateId, String payload, Instant createdAt, Instant processedAt, int retryCount) {

        validate(eventType, aggregateType, aggregateId, payload);

        if (createdAt == null) {
            throw new IllegalArgumentException("Created time cannot be null");
        }

        if (retryCount < 0) {
            throw new IllegalArgumentException("Retry count cannot be negative");
        }

        return new OutboxEvent(id, eventType, aggregateType, aggregateId, payload, createdAt, processedAt, retryCount);
    }

    public void markProcessed() {
        if (processedAt != null) {
            throw new IllegalStateException("Outbox event is already processed");
        }

        processedAt = Instant.now();
    }

    public void incrementRetryCount() {
        retryCount++;
    }

    private static void validate(String eventType, String aggregateType, String aggregateId, String payload) {

        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException("Event type cannot be empty");
        }

        if (aggregateType == null || aggregateType.isBlank()) {
            throw new IllegalArgumentException("Aggregate type cannot be empty");
        }

        if (aggregateId == null || aggregateId.isBlank()) {
            throw new IllegalArgumentException("Aggregate ID cannot be empty");
        }

        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Payload cannot be empty");
        }
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

    public Instant processedAt() {
        return processedAt;
    }

    public int retryCount() {
        return retryCount;
    }
}