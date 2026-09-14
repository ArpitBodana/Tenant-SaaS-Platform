package com.absys.saas.tenant.platform.order.application.command;

import java.util.UUID;

public record RemoveOrderItemCommand(
        UUID orderId,
        UUID orderItemId
) {
}