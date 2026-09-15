package com.absys.saas.tenant.platform.order.application.service;

import com.absys.saas.tenant.platform.customer.domain.repository.CustomerRepository;
import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.order.application.command.AddOrderItemCommand;
import com.absys.saas.tenant.platform.order.domain.model.Order;
import com.absys.saas.tenant.platform.order.domain.model.OrderId;
import com.absys.saas.tenant.platform.order.domain.repository.OrderRepository;
import com.absys.saas.tenant.platform.product.domain.model.Product;
import com.absys.saas.tenant.platform.product.domain.model.ProductId;
import com.absys.saas.tenant.platform.product.domain.model.ProductName;
import com.absys.saas.tenant.platform.product.domain.model.ProductSku;
import com.absys.saas.tenant.platform.product.domain.repository.ProductRepository;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderCommandServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    private final OrderCommandService service = new OrderCommandService(orderRepository, customerRepository, productRepository, eventPublisher);

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldUseProductPriceWhenAddingItem() {

        UUID tenantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        TenantContext.setTenantId(tenantId);

        Order order = Order.create(OrderId.of(orderId), tenantId, UUID.randomUUID());

        Product product = Product.create(ProductId.of(productId), tenantId, new ProductName("Laptop"), new ProductSku("LAPTOP-001"), new BigDecimal("50000.00"));

        when(orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantId)).thenReturn(Optional.of(order));

        when(productRepository.findByIdAndTenantId(ProductId.of(productId), tenantId)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.addItem(new AddOrderItemCommand(orderId, productId, 2)) == null ? null : order;

        assertNotNull(result);
        assertEquals(new BigDecimal("50000.00"), result.items().getFirst().unitPrice());

        assertEquals(new BigDecimal("100000.00"), result.totalAmount());
    }

    @Test
    void shouldRejectInactiveProduct() {

        UUID tenantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        TenantContext.setTenantId(tenantId);

        Order order = Order.create(OrderId.of(orderId), tenantId, UUID.randomUUID());

        Product product = Product.create(ProductId.of(productId), tenantId, new ProductName("Laptop"), new ProductSku("LAPTOP-001"), new BigDecimal("50000.00"));

        product.deactivate();

        when(orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantId)).thenReturn(Optional.of(order));

        when(productRepository.findByIdAndTenantId(ProductId.of(productId), tenantId)).thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class, () -> service.addItem(new AddOrderItemCommand(orderId, productId, 1)));

        assertTrue(order.items().isEmpty());

        verify(orderRepository, never()).save(any());
    }


    @Test
    void shouldNotAllowCrossTenantProductAccess() {

        UUID tenantA = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        TenantContext.setTenantId(tenantA);

        Order order = Order.create(OrderId.of(orderId), tenantA, UUID.randomUUID());

        when(orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantA)).thenReturn(Optional.of(order));

        when(productRepository.findByIdAndTenantId(ProductId.of(productId), tenantA)).thenReturn(Optional.empty());

        assertThrows(com.absys.saas.tenant.platform.shared.domain.exception.NotFoundException.class, () -> service.addItem(new AddOrderItemCommand(orderId, productId, 1)));

        assertTrue(order.items().isEmpty());

        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldPublishOrderConfirmedEvent() {

        UUID tenantId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        TenantContext.setTenantId(tenantId);

        Order order = Order.create(OrderId.of(orderId), tenantId, customerId);

        order.addItem(com.absys.saas.tenant.platform.order.domain.model.OrderItem.create(com.absys.saas.tenant.platform.order.domain.model.OrderItemId.generate(), UUID.randomUUID(), 2, new BigDecimal("100.00")));

        when(orderRepository.findByIdAndTenantId(OrderId.of(orderId), tenantId)).thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.confirm(new com.absys.saas.tenant.platform.order.application.command.ConfirmOrderCommand(orderId));

        verify(eventPublisher).publishEvent(argThat((Object event) -> event instanceof OrderConfirmedEvent && ((OrderConfirmedEvent) event).orderId().equals(orderId) && ((OrderConfirmedEvent) event).tenantId().equals(tenantId) && ((OrderConfirmedEvent) event).customerId().equals(customerId)));
    }


}