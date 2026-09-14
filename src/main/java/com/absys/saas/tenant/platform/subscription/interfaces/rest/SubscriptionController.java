package com.absys.saas.tenant.platform.subscription.interfaces.rest;

import com.absys.saas.tenant.platform.subscription.application.command.*;
import com.absys.saas.tenant.platform.subscription.application.dto.SubscriptionResponse;
import com.absys.saas.tenant.platform.subscription.application.query.*;
import com.absys.saas.tenant.platform.subscription.application.service.SubscriptionCommandService;
import com.absys.saas.tenant.platform.subscription.application.service.SubscriptionQueryService;
import com.absys.saas.tenant.platform.subscription.domain.model.BillingCycle;
import com.absys.saas.tenant.platform.subscription.domain.model.SubscriptionPlan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionCommandService commandService;
    private final SubscriptionQueryService queryService;

    public SubscriptionController(SubscriptionCommandService commandService, SubscriptionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(@Valid @RequestBody CreateSubscriptionRequest request) {

        return commandService.create(new CreateSubscriptionCommand(request.plan(), request.billingCycle()));
    }

    @GetMapping
    public SubscriptionResponse getTenantSubscription() {

        return queryService.getTenantSubscription(new GetTenantSubscriptionQuery());
    }

    @GetMapping("/{subscriptionId}")
    public SubscriptionResponse get(@PathVariable UUID subscriptionId) {

        return queryService.get(new GetSubscriptionQuery(subscriptionId));
    }

    @PatchMapping("/{subscriptionId}/plan")
    public SubscriptionResponse changePlan(@PathVariable UUID subscriptionId, @Valid @RequestBody ChangePlanRequest request) {

        return commandService.changePlan(new ChangePlanCommand(subscriptionId, request.plan()));
    }

    @PatchMapping("/{subscriptionId}/cancel")
    public SubscriptionResponse cancel(@PathVariable UUID subscriptionId) {

        return commandService.cancel(new CancelSubscriptionCommand(subscriptionId));
    }

    @PatchMapping("/{subscriptionId}/reactivate")
    public SubscriptionResponse reactivate(@PathVariable UUID subscriptionId) {

        return commandService.reactivate(new ReactivateSubscriptionCommand(subscriptionId));
    }

    public record CreateSubscriptionRequest(

            @NotNull SubscriptionPlan plan,

            @NotNull BillingCycle billingCycle) {
    }

    public record ChangePlanRequest(

            @NotNull SubscriptionPlan plan) {
    }
}