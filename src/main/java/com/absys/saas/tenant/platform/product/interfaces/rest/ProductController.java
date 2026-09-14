package com.absys.saas.tenant.platform.product.interfaces.rest;

import com.absys.saas.tenant.platform.product.application.command.*;
import com.absys.saas.tenant.platform.product.application.dto.ProductResponse;
import com.absys.saas.tenant.platform.product.application.query.*;
import com.absys.saas.tenant.platform.product.application.service.ProductCommandService;
import com.absys.saas.tenant.platform.product.application.service.ProductQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductCommandService commandService;
    private final ProductQueryService queryService;

    public ProductController(ProductCommandService commandService, ProductQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {

        return commandService.create(new CreateProductCommand(request.name(), request.sku(), request.price()));
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable UUID productId, @Valid @RequestBody UpdateProductRequest request) {

        return commandService.update(new UpdateProductCommand(productId, request.name(), request.sku(), request.price()));
    }

    @GetMapping("/{productId}")
    public ProductResponse get(@PathVariable UUID productId) {

        return queryService.get(new GetProductQuery(productId));
    }

    @GetMapping
    public List<ProductResponse> getAll() {

        return queryService.getAll(new GetProductsQuery());
    }

    @PatchMapping("/{productId}/activate")
    public ProductResponse activate(@PathVariable UUID productId) {

        return commandService.activate(new ActivateProductCommand(productId));
    }

    @PatchMapping("/{productId}/deactivate")
    public ProductResponse deactivate(@PathVariable UUID productId) {

        return commandService.deactivate(new DeactivateProductCommand(productId));
    }

    public record CreateProductRequest(

            @NotBlank @Size(max = 150) String name,

            @NotBlank @Size(max = 100) String sku,

            @NotNull @DecimalMin(value = "0.00") BigDecimal price) {
    }

    public record UpdateProductRequest(

            @NotBlank @Size(max = 150) String name,

            @NotBlank @Size(max = 100) String sku,

            @NotNull @DecimalMin(value = "0.00") BigDecimal price) {
    }
}