package com.absys.saas.tenant.platform.product.domain.repository;

import com.absys.saas.tenant.platform.product.domain.model.Product;
import com.absys.saas.tenant.platform.product.domain.model.ProductId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findByIdAndTenantId(ProductId productId, UUID tenantId);

    List<Product> findAllByTenantId(UUID tenantId);

    boolean existsBySkuAndTenantId(String sku, UUID tenantId);

    boolean existsBySkuAndTenantIdAndIdNot(String sku, UUID tenantId, ProductId productId);
}