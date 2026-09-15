package com.absys.saas.tenant.platform.notification.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.notification.application.dto.NotificationResponse;
import com.absys.saas.tenant.platform.notification.application.query.GetNotificationQuery;
import com.absys.saas.tenant.platform.notification.application.query.GetNotificationsQuery;
import com.absys.saas.tenant.platform.notification.domain.model.Notification;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationId;
import com.absys.saas.tenant.platform.notification.domain.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse get(GetNotificationQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Notification notification = notificationRepository.findByIdAndTenantId(NotificationId.of(query.notificationId()), tenantId).orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        return toResponse(notification);
    }

    public List<NotificationResponse> getAll(GetNotificationsQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        return notificationRepository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    private NotificationResponse toResponse(Notification notification) {

        return new NotificationResponse(notification.id().value(), notification.channel(), notification.recipient(), notification.subject(), notification.message(), notification.status(), notification.createdAt(), notification.sentAt(), notification.failureReason());
    }
}