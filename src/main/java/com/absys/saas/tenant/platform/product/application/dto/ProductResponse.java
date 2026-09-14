package com.absys.saas.tenant.platform.product.application.dto;

import com.absys.saas.tenant.platform.product.domain.model.ProductStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String sku,
        BigDecimal price,
        ProductStatus status
) {
}