package com.absys.saas.tenant.platform.subscription.application.dto;

import com.absys.saas.tenant.platform.subscription.domain.model.BillingCycle;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionPlan;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        SubscriptionPlan plan,
        BillingCycle billingCycle,
        SubscriptionStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
}