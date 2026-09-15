package com.absys.saas.tenant.platform.notification.application.query;

import java.util.UUID;

public record GetNotificationQuery(
        UUID notificationId
) {
}