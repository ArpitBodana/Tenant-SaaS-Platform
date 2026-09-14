package com.absys.saas.tenant.platform.subscription.application.command;

import java.util.UUID;

public record ReactivateSubscriptionCommand(
        UUID subscriptionId
) {
}