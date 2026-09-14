package com.absys.saas.tenant.platform.product.application.service;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.product.application.command.*;
import com.absys.saas.tenant.platform.product.application.dto.ProductResponse;
import com.absys.saas.tenant.platform.product.domain.model.*;
import com.absys.saas.tenant.platform.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProductCommandService {

    private final ProductRepository productRepository;

    public ProductCommandService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(CreateProductCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        ProductSku sku = new ProductSku(command.sku());

        if (productRepository.existsBySkuAndTenantId(sku.value(), tenantId)) {
            throw new IllegalStateException("Product SKU already exists");
        }

        Product product = Product.create(ProductId.generate(), tenantId, new ProductName(command.name()), sku, command.price());

        Product saved = productRepository.save(product);

        return toResponse(saved);
    }

    public ProductResponse update(UpdateProductCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Product product = productRepository.findByIdAndTenantId(ProductId.of(command.productId()), tenantId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ProductSku sku = new ProductSku(command.sku());

        if (productRepository.existsBySkuAndTenantIdAndIdNot(sku.value(), tenantId, product.id())) {
            throw new IllegalStateException("Product SKU already exists");
        }

        product.update(new ProductName(command.name()), sku, command.price());

        return toResponse(productRepository.save(product));
    }

    public ProductResponse activate(ActivateProductCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Product product = findProduct(command.productId(), tenantId);

        product.activate();

        return toResponse(productRepository.save(product));
    }

    public ProductResponse deactivate(DeactivateProductCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Product product = findProduct(command.productId(), tenantId);

        product.deactivate();

        return toResponse(productRepository.save(product));
    }

    private Product findProduct(UUID productId, UUID tenantId) {

        return productRepository.findByIdAndTenantId(ProductId.of(productId), tenantId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    private ProductResponse toResponse(Product product) {

        return new ProductResponse(product.id().value(), product.name().value(), product.sku().value(), product.price(), product.status());
    }
}