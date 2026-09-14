package com.absys.saas.tenant.platform.product.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        String name,
        String sku,
        BigDecimal price
) {
}