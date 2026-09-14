package com.absys.saas.tenant.platform.subscription.application.command;

import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionPlan;

import java.util.UUID;

public record ChangePlanCommand(
        UUID subscriptionId,
        SubscriptionPlan plan
) {
}