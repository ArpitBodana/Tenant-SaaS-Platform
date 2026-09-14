package com.absys.saas.tenant.platform.product.infrastructure.persistence;

import com.absys.saas.tenant.platform.product.domain.model.*;
import com.absys.saas.tenant.platform.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SpringDataProductRepository repository;

    public ProductRepositoryImpl(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {

        ProductJpaEntity entity = toEntity(product);

        ProductJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Product> findByIdAndTenantId(ProductId productId, UUID tenantId) {

        return repository.findByIdAndTenantId(productId.value(), tenantId).map(this::toDomain);
    }

    @Override
    public List<Product> findAllByTenantId(UUID tenantId) {

        return repository.findAllByTenantId(tenantId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsBySkuAndTenantId(String sku, UUID tenantId) {

        return repository.existsBySkuAndTenantId(sku, tenantId);
    }

    @Override
    public boolean existsBySkuAndTenantIdAndIdNot(String sku, UUID tenantId, ProductId productId) {

        return repository.existsBySkuAndTenantIdAndIdNot(sku, tenantId, productId.value());
    }

    private ProductJpaEntity toEntity(Product product) {

        return new ProductJpaEntity(product.id().value(), product.tenantId(), product.name().value(), product.sku().value(), product.price(), product.status().name());
    }

    private Product toDomain(ProductJpaEntity entity) {

        return Product.restore(ProductId.of(entity.getId()), entity.getTenantId(), new ProductName(entity.getName()), new ProductSku(entity.getSku()), entity.getPrice(), ProductStatus.valueOf(entity.getStatus()));
    }
}