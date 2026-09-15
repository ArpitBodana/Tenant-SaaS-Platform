package com.absys.saas.tenant.platform.order.application.service;

import com.absys.saas.tenant.platform.customer.domain.model.Customer;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerId;
import com.absys.saas.tenant.platform.customer.domain.repository.CustomerRepository;
import com.absys.saas.tenant.platform.order.application.command.AddOrderItemCommand;
import com.absys.saas.tenant.platform.order.application.command.CancelOrderCommand;
import com.absys.saas.tenant.platform.order.application.command.ConfirmOrderCommand;
import com.absys.saas.tenant.platform.order.application.command.CreateOrderCommand;
import com.absys.saas.tenant.platform.order.application.command.RemoveOrderItemCommand;
import com.absys.saas.tenant.platform.order.application.dto.OrderItemResponse;
import com.absys.saas.tenant.platform.order.application.dto.OrderResponse;
import com.absys.saas.tenant.platform.order.domain.model.Order;
import com.absys.saas.tenant.platform.order.domain.model.OrderId;
import com.absys.saas.tenant.platform.order.domain.model.OrderItem;
import com.absys.saas.tenant.platform.order.domain.model.OrderItemId;
import com.absys.saas.tenant.platform.order.domain.repository.OrderRepository;
import com.absys.saas.tenant.platform.outbox.application.service.OutboxService;
import com.absys.saas.tenant.platform.product.domain.model.Product;
import com.absys.saas.tenant.platform.product.domain.model.ProductId;
import com.absys.saas.tenant.platform.product.domain.model.ProductStatus;
import com.absys.saas.tenant.platform.product.domain.repository.ProductRepository;
import com.absys.saas.tenant.platform.shared.domain.exception.NotFoundException;
import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OutboxService outboxService;


    public OrderResponse create(CreateOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(CustomerId.of(command.customerId()), tenantId).orElseThrow(() -> new NotFoundException("Customer not found"));

        Order order = Order.create(OrderId.generate(), tenantId, customer.id().value());

        return toResponse(orderRepository.save(order));
    }

    public OrderResponse addItem(AddOrderItemCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        Product product = productRepository.findByIdAndTenantId(ProductId.of(command.productId()), tenantId).orElseThrow(() -> new NotFoundException("Product not found"));

        if (product.status() != ProductStatus.ACTIVE) {
            throw new IllegalStateException("Inactive product cannot be added to an order");
        }

        OrderItem item = OrderItem.create(OrderItemId.generate(), product.id().value(), command.quantity(), product.price());

        order.addItem(item);

        return toResponse(orderRepository.save(order));
    }

    public OrderResponse removeItem(RemoveOrderItemCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.removeItem(OrderItemId.of(command.orderItemId()));

        return toResponse(orderRepository.save(order));
    }

    public OrderResponse confirm(ConfirmOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.confirm();

        Order savedOrder = orderRepository.save(order);

        outboxService.store(new OrderConfirmedEvent(savedOrder.id().value(), savedOrder.tenantId(), savedOrder.customerId(), savedOrder.totalAmount(), Instant.now()));

        return toResponse(savedOrder);
    }

    public OrderResponse cancel(CancelOrderCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Order order = findOrder(command.orderId(), tenantId);

        order.cancel();

        return toResponse(orderRepository.save(order));
    }

    private Order findOrder(UUID orderId, UUID tenantId) {

        return orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantId).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    private OrderResponse toResponse(Order order) {

        return new OrderResponse(order.id().value(), order.customerId(), order.status(), order.items().stream().map(item -> new OrderItemResponse(item.id().value(), item.productId(), item.quantity(), item.unitPrice(), item.subtotal())).toList(), order.totalAmount());
    }
}