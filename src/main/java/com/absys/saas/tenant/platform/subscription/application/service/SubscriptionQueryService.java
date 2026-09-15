package com.absys.saas.tenant.platform.subscription.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.shared.domain.exception.NotFoundException;
import com.absys.saas.tenant.platform.subscription.application.dto.SubscriptionResponse;
import com.absys.saas.tenant.platform.subscription.application.query.*;
import com.absys.saas.tenant.platform.subscription.domain.model.Subscription;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionId;
import com.absys.saas.tenant.platform.subscription.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionResponse get(GetSubscriptionQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Subscription subscription = subscriptionRepository.findByIdAndTenantId(SubscriptionId.of(query.subscriptionId()), tenantId).orElseThrow(() -> new NotFoundException("Subscription not found"));

        return toResponse(subscription);
    }

    public SubscriptionResponse getTenantSubscription(GetTenantSubscriptionQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Subscription subscription = subscriptionRepository.findActiveByTenantId(tenantId).orElseThrow(() -> new IllegalArgumentException("Active subscription not found"));

        return toResponse(subscription);
    }

    private SubscriptionResponse toResponse(Subscription subscription) {

        return new SubscriptionResponse(subscription.id().value(), subscription.plan(), subscription.billingCycle(), subscription.status(), subscription.startDate(), subscription.endDate());
    }
}