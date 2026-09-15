package com.absys.saas.tenant.platform.notification.application.command;

import com.absys.saas.tenant.platform.notification.domain.model.NotificationChannel;

public record CreateNotificationCommand(
        NotificationChannel channel,
        String recipient,
        String subject,
        String message
) {
}