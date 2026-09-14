package com.absys.saas.tenant.platform.subscription.application.query;

import java.util.UUID;

public record GetSubscriptionQuery(
        UUID subscriptionId
) {
}