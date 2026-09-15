package com.absys.saas.tenant.platform.order.interfaces.rest;

import com.absys.saas.tenant.platform.order.application.command.*;
import com.absys.saas.tenant.platform.order.application.dto.OrderResponse;
import com.absys.saas.tenant.platform.order.application.query.GetOrderQuery;
import com.absys.saas.tenant.platform.order.application.query.GetOrdersQuery;
import com.absys.saas.tenant.platform.order.application.service.OrderCommandService;
import com.absys.saas.tenant.platform.order.application.service.OrderQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderCommandService commandService;
    private final OrderQueryService queryService;

    public OrderController(OrderCommandService commandService, OrderQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {

        return commandService.create(new CreateOrderCommand(request.customerId()));
    }

    @PostMapping("/{orderId}/items")
    public OrderResponse addItem(@PathVariable UUID orderId, @Valid @RequestBody AddOrderItemRequest request) {
        return commandService.addItem(new AddOrderItemCommand(orderId, request.productId(), request.quantity()));
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    public OrderResponse removeItem(@PathVariable UUID orderId, @PathVariable UUID orderItemId) {

        return commandService.removeItem(new RemoveOrderItemCommand(orderId, orderItemId));
    }

    @PatchMapping("/{orderId}/confirm")
    public OrderResponse confirm(@PathVariable UUID orderId) {

        return commandService.confirm(new ConfirmOrderCommand(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancel(@PathVariable UUID orderId) {

        return commandService.cancel(new CancelOrderCommand(orderId));
    }

    @GetMapping("/{orderId}")
    public OrderResponse get(@PathVariable UUID orderId) {

        return queryService.get(new GetOrderQuery(orderId));
    }

    @GetMapping
    public List<OrderResponse> getAll() {

        return queryService.getAll(new GetOrdersQuery());
    }

    public record CreateOrderRequest(@NotNull UUID customerId) {
    }

    public record AddOrderItemRequest(@NotNull UUID productId, @Min(1) int quantity) {
    }
}