package com.absys.saas.tenant.platform.order.application.command;

import java.util.UUID;

public record ConfirmOrderCommand(
        UUID orderId
) {
}