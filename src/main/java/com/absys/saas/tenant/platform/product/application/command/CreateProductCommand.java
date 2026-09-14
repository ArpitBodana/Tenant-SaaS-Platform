package com.absys.saas.tenant.platform.product.application.command;

import java.math.BigDecimal;

public record CreateProductCommand(
        String name,
        String sku,
        BigDecimal price
) {
}