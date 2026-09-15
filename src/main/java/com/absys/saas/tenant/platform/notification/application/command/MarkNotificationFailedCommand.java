package com.absys.saas.tenant.platform.notification.application.command;

import java.util.UUID;

public record MarkNotificationFailedCommand(
        UUID notificationId,
        String reason
) {
}