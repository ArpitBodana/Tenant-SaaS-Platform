package com.absys.saas.tenant.platform.product.application.query;

import java.util.UUID;

public record GetProductQuery(
        UUID productId
) {
}