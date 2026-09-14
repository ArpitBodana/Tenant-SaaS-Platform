package com.absys.saas.tenant.platform.order.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.order.application.dto.*;
import com.absys.saas.tenant.platform.order.application.query.*;
import com.absys.saas.tenant.platform.order.domain.model.*;
import com.absys.saas.tenant.platform.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse get(GetOrderQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = orderRepository.findByIdAndTenantId(OrderId.of(query.orderId()), tenantId).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        return toResponse(order);
    }

    public List<OrderResponse> getAll(GetOrdersQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        return orderRepository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(order.id().value(), order.customerId(), order.status(), order.items().stream().map(item -> new OrderItemResponse(item.id().value(), item.productId(), item.quantity(), item.unitPrice(), item.subtotal())).toList(), order.totalAmount());
    }
}