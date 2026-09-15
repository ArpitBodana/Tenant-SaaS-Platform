package com.absys.saas.tenant.platform.shared.application.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderConfirmedEvent(
        UUID orderId,
        UUID tenantId,
        UUID customerId,
        BigDecimal totalAmount,
        Instant occurredAt
) {
}