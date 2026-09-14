package com.absys.saas.tenant.platform.subscription.application.command;

import com.absys.saas.tenant.platform.subscription.domain.model.BillingCycle;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionPlan;

public record CreateSubscriptionCommand(
        SubscriptionPlan plan,
        BillingCycle billingCycle
) {
}