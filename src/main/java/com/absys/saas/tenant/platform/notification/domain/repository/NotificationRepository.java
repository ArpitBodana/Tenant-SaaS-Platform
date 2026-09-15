package com.absys.saas.tenant.platform.notification.domain.repository;

import com.absys.saas.tenant.platform.notification.domain.model.Notification;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findByIdAndTenantId(NotificationId notificationId, UUID tenantId);

    List<Notification> findAllByTenantId(UUID tenantId);
}