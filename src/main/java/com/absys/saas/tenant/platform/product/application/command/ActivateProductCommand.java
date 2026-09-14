package com.absys.saas.tenant.platform.product.application.command;

import java.util.UUID;

public record ActivateProductCommand(
        UUID productId
) {
}