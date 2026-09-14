package com.absys.saas.tenant.platform.subscription.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.subscription.application.command.*;
import com.absys.saas.tenant.platform.subscription.application.dto.SubscriptionResponse;
import com.absys.saas.tenant.platform.subscription.domain.model.*;
import com.absys.saas.tenant.platform.subscription.domain.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionCommandService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionResponse create(CreateSubscriptionCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        if (subscriptionRepository.existsActiveByTenantId(tenantId)) {
            throw new IllegalStateException("Tenant already has an active subscription");
        }

        LocalDate startDate = LocalDate.now();

        LocalDate endDate = calculateEndDate(startDate, command.billingCycle());

        Subscription subscription = Subscription.create(SubscriptionId.generate(), tenantId, command.plan(), command.billingCycle(), startDate, endDate);

        return toResponse(subscriptionRepository.save(subscription));
    }

    public SubscriptionResponse changePlan(ChangePlanCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Subscription subscription = findSubscription(command.subscriptionId(), tenantId);

        subscription.changePlan(command.plan());

        return toResponse(subscriptionRepository.save(subscription));
    }

    public SubscriptionResponse cancel(CancelSubscriptionCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Subscription subscription = findSubscription(command.subscriptionId(), tenantId);

        subscription.cancel();

        return toResponse(subscriptionRepository.save(subscription));
    }

    public SubscriptionResponse reactivate(ReactivateSubscriptionCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Subscription subscription = findSubscription(command.subscriptionId(), tenantId);

        subscription.reactivate();

        return toResponse(subscriptionRepository.save(subscription));
    }

    private Subscription findSubscription(UUID subscriptionId, UUID tenantId) {

        return subscriptionRepository.findByIdAndTenantId(SubscriptionId.of(subscriptionId), tenantId).orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
    }

    private LocalDate calculateEndDate(LocalDate startDate, BillingCycle billingCycle) {

        return switch (billingCycle) {

            case MONTHLY -> startDate.plusMonths(1);

            case YEARLY -> startDate.plusYears(1);
        };
    }

    private SubscriptionResponse toResponse(Subscription subscription) {

        return new SubscriptionResponse(subscription.id().value(), subscription.plan(), subscription.billingCycle(), subscription.status(), subscription.startDate(), subscription.endDate());
    }
}