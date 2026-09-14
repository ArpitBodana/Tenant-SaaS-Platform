package com.absys.saas.tenant.platform.order.application.query;

import java.util.UUID;

public record GetOrderQuery(
        UUID orderId
) {
}