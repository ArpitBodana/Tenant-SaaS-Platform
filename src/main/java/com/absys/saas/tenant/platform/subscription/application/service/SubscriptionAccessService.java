package com.absys.saas.tenant.platform.subscription.application.service;

import com.absys.saas.tenant.platform.subscription.application.exception.InactiveSubscriptionException;
import com.absys.saas.tenant.platform.subscription.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SubscriptionAccessService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionAccessService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public boolean hasActiveSubscription(UUID tenantId) {

        return subscriptionRepository.existsActiveByTenantId(tenantId);
    }

    public void requireActiveSubscription(UUID tenantId) {

        if (!hasActiveSubscription(tenantId)) {

            throw new InactiveSubscriptionException("Tenant does not have an active subscription");
        }
    }
}