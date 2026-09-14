package com.absys.saas.tenant.platform.product.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.product.application.dto.ProductResponse;
import com.absys.saas.tenant.platform.product.application.query.GetProductQuery;
import com.absys.saas.tenant.platform.product.application.query.GetProductsQuery;
import com.absys.saas.tenant.platform.product.domain.model.Product;
import com.absys.saas.tenant.platform.product.domain.model.ProductId;
import com.absys.saas.tenant.platform.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse get(GetProductQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Product product = productRepository.findByIdAndTenantId(ProductId.of(query.productId()), tenantId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return toResponse(product);
    }

    public List<ProductResponse> getAll(GetProductsQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        return productRepository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(product.id().value(), product.name().value(), product.sku().value(), product.price(), product.status());
    }
}