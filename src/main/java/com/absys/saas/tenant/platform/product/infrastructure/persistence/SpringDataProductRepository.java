package com.absys.saas.tenant.platform.product.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    List<ProductJpaEntity> findAllByTenantId(UUID tenantId);

    boolean existsBySkuAndTenantId(String sku, UUID tenantId);

    boolean existsBySkuAndTenantIdAndIdNot(String sku, UUID tenantId, UUID id);
}