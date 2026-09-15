package com.absys.saas.tenant.platform.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Notification {

    private final NotificationId id;
    private final UUID tenantId;

    private final NotificationChannel channel;

    private final String recipient;
    private final String subject;
    private final String message;

    private NotificationStatus status;

    private Instant createdAt;
    private Instant sentAt;

    private String failureReason;

    private Notification(NotificationId id, UUID tenantId, NotificationChannel channel, String recipient, String subject, String message, NotificationStatus status, Instant createdAt, Instant sentAt, String failureReason) {
        this.id = id;
        this.tenantId = tenantId;
        this.channel = channel;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.failureReason = failureReason;
    }

    public static Notification create(NotificationId id, UUID tenantId, NotificationChannel channel, String recipient, String subject, String message) {

        validate(tenantId, channel, recipient, message);

        if (channel == NotificationChannel.EMAIL && (subject == null || subject.isBlank())) {

            throw new IllegalArgumentException("Subject is required for email notifications");
        }

        return new Notification(id, tenantId, channel, recipient.trim(), normalizeSubject(subject), message.trim(), NotificationStatus.PENDING, Instant.now(), null, null);
    }

    public static Notification restore(NotificationId id, UUID tenantId, NotificationChannel channel, String recipient, String subject, String message, NotificationStatus status, Instant createdAt, Instant sentAt, String failureReason) {

        validate(tenantId, channel, recipient, message);

        if (status == null) {
            throw new IllegalArgumentException("Notification status cannot be null");
        }

        if (createdAt == null) {
            throw new IllegalArgumentException("Created time cannot be null");
        }

        return new Notification(id, tenantId, channel, recipient, subject, message, status, createdAt, sentAt, failureReason);
    }

    public void markSent() {

        if (status == NotificationStatus.SENT) {
            throw new IllegalStateException("Notification is already sent");
        }

        status = NotificationStatus.SENT;
        sentAt = Instant.now();
        failureReason = null;
    }

    public void markFailed(String reason) {

        if (status == NotificationStatus.SENT) {
            throw new IllegalStateException("Sent notification cannot be marked as failed");
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason cannot be empty");
        }

        status = NotificationStatus.FAILED;
        failureReason = reason.trim();
    }

    private static void validate(UUID tenantId, NotificationChannel channel, String recipient, String message) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        if (channel == null) {
            throw new IllegalArgumentException("Notification channel cannot be null");
        }

        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Notification recipient cannot be empty");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Notification message cannot be empty");
        }

        if (recipient.length() > 255) {
            throw new IllegalArgumentException("Notification recipient cannot exceed 255 characters");
        }

        if (message.length() > 5000) {
            throw new IllegalArgumentException("Notification message cannot exceed 5000 characters");
        }
    }

    private static String normalizeSubject(String subject) {

        if (subject == null || subject.isBlank()) {
            return null;
        }

        String value = subject.trim();

        if (value.length() > 255) {
            throw new IllegalArgumentException("Notification subject cannot exceed 255 characters");
        }

        return value;
    }

    public NotificationId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public NotificationChannel channel() {
        return channel;
    }

    public String recipient() {
        return recipient;
    }

    public String subject() {
        return subject;
    }

    public String message() {
        return message;
    }

    public NotificationStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant sentAt() {
        return sentAt;
    }

    public String failureReason() {
        return failureReason;
    }
}