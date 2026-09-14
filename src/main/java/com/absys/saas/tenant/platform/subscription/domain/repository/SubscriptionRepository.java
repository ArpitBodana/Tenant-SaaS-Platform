package com.absys.saas.tenant.platform.subscription.domain.repository;

import com.absys.saas.tenant.platform.subscription.domain.model.Subscription;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionId;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Subscription save(Subscription subscription);

    Optional<Subscription> findByIdAndTenantId(SubscriptionId subscriptionId, UUID tenantId);

    Optional<Subscription> findActiveByTenantId(UUID tenantId);

    boolean existsActiveByTenantId(UUID tenantId);
}