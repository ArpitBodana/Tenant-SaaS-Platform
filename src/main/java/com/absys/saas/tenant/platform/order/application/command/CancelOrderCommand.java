package com.absys.saas.tenant.platform.order.application.command;

import java.util.UUID;

public record CancelOrderCommand(
        UUID orderId
) {
}