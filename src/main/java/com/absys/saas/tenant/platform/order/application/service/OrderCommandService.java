package com.absys.saas.tenant.platform.order.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.order.application.command.*;
import com.absys.saas.tenant.platform.order.application.dto.OrderItemResponse;
import com.absys.saas.tenant.platform.order.application.dto.OrderResponse;
import com.absys.saas.tenant.platform.order.domain.model.*;
import com.absys.saas.tenant.platform.order.domain.repository.OrderRepository;
import com.absys.saas.tenant.platform.subscription.application.security.RequiresActiveSubscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrderCommandService {

    private final OrderRepository orderRepository;

    public OrderCommandService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RequiresActiveSubscription
    public OrderResponse create(CreateOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = Order.create(OrderId.generate(), tenantId, command.customerId());

        return toResponse(orderRepository.save(order));
    }

    @RequiresActiveSubscription
    public OrderResponse addItem(AddOrderItemCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        OrderItem item = OrderItem.create(OrderItemId.generate(), command.productId(), command.quantity(), command.unitPrice());

        order.addItem(item);

        return toResponse(orderRepository.save(order));
    }

    @RequiresActiveSubscription
    public OrderResponse removeItem(RemoveOrderItemCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.removeItem(OrderItemId.of(command.orderItemId()));

        return toResponse(orderRepository.save(order));
    }

    @RequiresActiveSubscription
    public OrderResponse confirm(ConfirmOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.confirm();

        return toResponse(orderRepository.save(order));
    }

    @RequiresActiveSubscription
    public OrderResponse cancel(CancelOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.cancel();

        return toResponse(orderRepository.save(order));
    }

    private Order findOrder(UUID orderId, UUID tenantId) {

        return orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(order.id().value(), order.customerId(), order.status(), order.items().stream().map(item -> new OrderItemResponse(item.id().value(), item.productId(), item.quantity(), item.unitPrice(), item.subtotal())).toList(), order.totalAmount());
    }
}