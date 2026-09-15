package com.absys.saas.tenant.platform.notification.infrastructure.persistence;

import com.absys.saas.tenant.platform.notification.domain.model.Notification;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationId;
import com.absys.saas.tenant.platform.notification.domain.repository.NotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final SpringDataNotificationRepository repository;

    public NotificationRepositoryImpl(SpringDataNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {

        NotificationJpaEntity saved = repository.save(toEntity(notification));

        return toDomain(saved);
    }

    @Override
    public Optional<Notification> findByIdAndTenantId(NotificationId notificationId, UUID tenantId) {

        return repository.findByIdAndTenantId(notificationId.value(), tenantId).map(this::toDomain);
    }

    @Override
    public List<Notification> findAllByTenantId(UUID tenantId) {

        return repository.findAllByTenantIdOrderByCreatedAtDesc(tenantId).stream().map(this::toDomain).toList();
    }

    private NotificationJpaEntity toEntity(Notification notification) {

        return new NotificationJpaEntity(notification.id().value(), notification.tenantId(), notification.channel(), notification.recipient(), notification.subject(), notification.message(), notification.status(), notification.createdAt(), notification.sentAt(), notification.failureReason());
    }

    private Notification toDomain(NotificationJpaEntity entity) {

        return Notification.restore(NotificationId.of(entity.getId()), entity.getTenantId(), entity.getChannel(), entity.getRecipient(), entity.getSubject(), entity.getMessage(), entity.getStatus(), entity.getCreatedAt(), entity.getSentAt(), entity.getFailureReason());
    }
}