package com.absys.saas.tenant.platform.subscription.infrastructure.persistence;

import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, UUID> {

    Optional<SubscriptionJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<SubscriptionJpaEntity> findByTenantIdAndStatus(UUID tenantId, SubscriptionStatus status);

    boolean existsByTenantIdAndStatus(UUID tenantId, SubscriptionStatus status);
}