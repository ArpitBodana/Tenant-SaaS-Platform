package com.absys.saas.tenant.platform.notification.application.dto;

import com.absys.saas.tenant.platform.notification.domain.model.NotificationChannel;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationStatus;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationChannel channel,
        String recipient,
        String subject,
        String message,
        NotificationStatus status,
        Instant createdAt,
        Instant sentAt,
        String failureReason
) {
}