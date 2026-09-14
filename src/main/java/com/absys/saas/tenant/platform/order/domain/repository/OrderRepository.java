package com.absys.saas.tenant.platform.order.domain.repository;

import com.absys.saas.tenant.platform.order.domain.model.Order;
import com.absys.saas.tenant.platform.order.domain.model.OrderId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByIdAndTenantId(OrderId orderId, UUID tenantId);

    List<Order> findAllByTenantId(UUID tenantId);
}