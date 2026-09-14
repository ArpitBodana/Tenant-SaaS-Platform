package com.absys.saas.tenant.platform.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrderRepository extends JpaRepository<OrderJpaEntity, UUID> {

    @Query("""
                select distinct o
                from OrderJpaEntity o
                left join fetch o.items
                where o.id = :id
                and o.tenantId = :tenantId
            """)
    Optional<OrderJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("""
                select distinct o
                from OrderJpaEntity o
                left join fetch o.items
                where o.tenantId = :tenantId
                order by o.id
            """)
    List<OrderJpaEntity> findAllByTenantId(UUID tenantId);
}