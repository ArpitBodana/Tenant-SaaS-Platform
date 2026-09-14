package com.absys.saas.tenant.platform.order.application.dto;

import com.absys.saas.tenant.platform.order.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        List<OrderItemResponse> items,
        BigDecimal totalAmount
) {
}