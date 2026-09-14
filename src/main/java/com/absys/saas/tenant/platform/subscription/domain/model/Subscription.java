package com.absys.saas.tenant.platform.subscription.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Subscription {

    private final SubscriptionId id;
    private final UUID tenantId;

    private SubscriptionPlan plan;
    private BillingCycle billingCycle;
    private SubscriptionStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private Subscription(SubscriptionId id, UUID tenantId, SubscriptionPlan plan, BillingCycle billingCycle, SubscriptionStatus status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.tenantId = tenantId;
        this.plan = plan;
        this.billingCycle = billingCycle;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Creates a NEW subscription.
     */
    public static Subscription create(SubscriptionId id, UUID tenantId, SubscriptionPlan plan, BillingCycle billingCycle, LocalDate startDate, LocalDate endDate) {

        validate(tenantId, plan, billingCycle, startDate, endDate);

        return new Subscription(id, tenantId, plan, billingCycle, SubscriptionStatus.ACTIVE, startDate, endDate);
    }

    /**
     * Restores an EXISTING subscription from persistence.
     */
    public static Subscription restore(SubscriptionId id, UUID tenantId, SubscriptionPlan plan, BillingCycle billingCycle, SubscriptionStatus status, LocalDate startDate, LocalDate endDate) {

        validate(tenantId, plan, billingCycle, startDate, endDate);

        if (status == null) {
            throw new IllegalArgumentException("Subscription status cannot be null");
        }

        return new Subscription(id, tenantId, plan, billingCycle, status, startDate, endDate);
    }

    public void changePlan(SubscriptionPlan newPlan) {

        if (status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can change plan");
        }

        if (newPlan == null) {
            throw new IllegalArgumentException("Subscription plan cannot be null");
        }

        if (this.plan == newPlan) {
            throw new IllegalStateException("Subscription is already on this plan");
        }

        this.plan = newPlan;
    }

    public void cancel() {

        if (status == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled");
        }

        if (status == SubscriptionStatus.EXPIRED) {
            throw new IllegalStateException("Expired subscription cannot be cancelled");
        }

        status = SubscriptionStatus.CANCELLED;
    }

    public void reactivate() {

        if (status != SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Only cancelled subscriptions can be reactivated");
        }

        status = SubscriptionStatus.ACTIVE;
    }

    public void suspend() {

        if (status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be suspended");
        }

        status = SubscriptionStatus.SUSPENDED;
    }

    public void expire() {

        if (status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can expire");
        }

        status = SubscriptionStatus.EXPIRED;
    }

    private static void validate(UUID tenantId, SubscriptionPlan plan, BillingCycle billingCycle, LocalDate startDate, LocalDate endDate) {

        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant ID cannot be null");
        }

        if (plan == null) {
            throw new IllegalArgumentException("Subscription plan cannot be null");
        }

        if (billingCycle == null) {
            throw new IllegalArgumentException("Billing cycle cannot be null");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    public SubscriptionId id() {
        return id;
    }

    public UUID tenantId() {
        return tenantId;
    }

    public SubscriptionPlan plan() {
        return plan;
    }

    public BillingCycle billingCycle() {
        return billingCycle;
    }

    public SubscriptionStatus status() {
        return status;
    }

    public LocalDate startDate() {
        return startDate;
    }

    public LocalDate endDate() {
        return endDate;
    }
}