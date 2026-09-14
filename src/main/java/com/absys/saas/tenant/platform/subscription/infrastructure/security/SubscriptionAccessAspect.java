package com.absys.saas.tenant.platform.subscription.infrastructure.security;

import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;
import com.absys.saas.tenant.platform.subscription.application.security.RequiresActiveSubscription;
import com.absys.saas.tenant.platform.subscription.application.service.SubscriptionAccessService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class SubscriptionAccessAspect {

    private final SubscriptionAccessService subscriptionAccessService;

    public SubscriptionAccessAspect(SubscriptionAccessService subscriptionAccessService) {
        this.subscriptionAccessService = subscriptionAccessService;
    }

    @Before("@annotation(com.absys.saas.subscription.application.security.RequiresActiveSubscription)")
    public void checkSubscription() {

        UUID tenantId = TenantContext.requireTenantId();

        subscriptionAccessService.requireActiveSubscription(tenantId);
    }
}