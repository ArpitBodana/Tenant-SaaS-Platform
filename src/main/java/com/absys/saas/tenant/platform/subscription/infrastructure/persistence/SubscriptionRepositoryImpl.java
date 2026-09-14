package com.absys.saas.tenant.platform.subscription.infrastructure.persistence;

import com.absys.saas.tenant.platform.subscription.domain.model.*;
import com.absys.saas.tenant.platform.subscription.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SpringDataSubscriptionRepository repository;

    public SubscriptionRepositoryImpl(SpringDataSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subscription save(Subscription subscription) {

        SubscriptionJpaEntity entity = toEntity(subscription);

        SubscriptionJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Subscription> findByIdAndTenantId(SubscriptionId subscriptionId, UUID tenantId) {

        return repository.findByIdAndTenantId(subscriptionId.value(), tenantId).map(this::toDomain);
    }

    @Override
    public Optional<Subscription> findActiveByTenantId(UUID tenantId) {

        return repository.findByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE).map(this::toDomain);
    }

    @Override
    public boolean existsActiveByTenantId(UUID tenantId) {

        return repository.existsByTenantIdAndStatus(tenantId, SubscriptionStatus.ACTIVE);
    }

    private SubscriptionJpaEntity toEntity(Subscription subscription) {

        return new SubscriptionJpaEntity(subscription.id().value(), subscription.tenantId(), subscription.plan(), subscription.billingCycle(), subscription.status(), subscription.startDate(), subscription.endDate());
    }

    private Subscription toDomain(SubscriptionJpaEntity entity) {

        return Subscription.restore(SubscriptionId.of(entity.getId()), entity.getTenantId(), entity.getPlan(), entity.getBillingCycle(), entity.getStatus(), entity.getStartDate(), entity.getEndDate());
    }
}