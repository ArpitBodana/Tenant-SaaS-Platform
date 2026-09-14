package com.absys.saas.tenant.platform.order.infrastructure.persistence;

import com.absys.saas.tenant.platform.order.domain.model.*;
import com.absys.saas.tenant.platform.order.domain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SpringDataOrderRepository repository;

    public OrderRepositoryImpl(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {

        OrderJpaEntity entity = toEntity(order);

        OrderJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Order> findByIdAndTenantId(OrderId orderId, UUID tenantId) {

        return repository.findByIdAndTenantId(orderId.value(), tenantId).map(this::toDomain);
    }

    @Override
    public List<Order> findAllByTenantId(UUID tenantId) {

        return repository.findAllByTenantId(tenantId).stream().map(this::toDomain).toList();
    }

    private OrderJpaEntity toEntity(Order order) {

        OrderJpaEntity entity = new OrderJpaEntity(order.id().value(), order.tenantId(), order.customerId(), order.status());

        for (OrderItem item : order.items()) {

            OrderItemJpaEntity itemEntity = new OrderItemJpaEntity(item.id().value(), item.productId(), item.quantity(), item.unitPrice());

            entity.addItem(itemEntity);
        }

        return entity;
    }

    private Order toDomain(OrderJpaEntity entity) {

        List<OrderItem> items = entity.getItems().stream().map(item -> OrderItem.restore(OrderItemId.of(item.getId()), item.getProductId(), item.getQuantity(), item.getUnitPrice())).toList();

        return Order.restore(OrderId.of(entity.getId()), entity.getTenantId(), entity.getCustomerId(), entity.getStatus(), items);
    }
}