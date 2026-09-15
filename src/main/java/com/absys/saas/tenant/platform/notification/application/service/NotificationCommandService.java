package com.absys.saas.tenant.platform.notification.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.notification.application.command.CreateNotificationCommand;
import com.absys.saas.tenant.platform.notification.application.command.MarkNotificationFailedCommand;
import com.absys.saas.tenant.platform.notification.application.command.MarkNotificationSentCommand;
import com.absys.saas.tenant.platform.notification.application.dto.NotificationResponse;
import com.absys.saas.tenant.platform.notification.domain.model.Notification;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationId;
import com.absys.saas.tenant.platform.notification.domain.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;

    public NotificationCommandService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse create(CreateNotificationCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Notification notification = Notification.create(NotificationId.generate(), tenantId, command.channel(), command.recipient(), command.subject(), command.message());

        return toResponse(notificationRepository.save(notification));
    }

    public NotificationResponse markSent(MarkNotificationSentCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Notification notification = findNotification(command.notificationId(), tenantId);

        notification.markSent();

        return toResponse(notificationRepository.save(notification));
    }

    public NotificationResponse markFailed(MarkNotificationFailedCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Notification notification = findNotification(command.notificationId(), tenantId);

        notification.markFailed(command.reason());

        return toResponse(notificationRepository.save(notification));
    }


    public NotificationResponse createForTenant(UUID tenantId, CreateNotificationCommand command) {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        return createInternal(tenantId, command);
    }

    private NotificationResponse createInternal(UUID tenantId, CreateNotificationCommand command) {

        Notification notification = Notification.create(NotificationId.generate(), tenantId, command.channel(), command.recipient(), command.subject(), command.message());

        return toResponse(notificationRepository.save(notification));
    }

    private Notification findNotification(UUID notificationId, UUID tenantId) {

        return notificationRepository.findByIdAndTenantId(NotificationId.of(notificationId), tenantId).orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }

    private NotificationResponse toResponse(Notification notification) {

        return new NotificationResponse(notification.id().value(), notification.channel(), notification.recipient(), notification.subject(), notification.message(), notification.status(), notification.createdAt(), notification.sentAt(), notification.failureReason());
    }
}