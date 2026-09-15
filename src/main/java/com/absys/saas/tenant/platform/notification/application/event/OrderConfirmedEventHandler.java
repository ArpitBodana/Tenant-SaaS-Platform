package com.absys.saas.tenant.platform.notification.application.event;

import com.absys.saas.tenant.platform.notification.application.command.CreateNotificationCommand;
import com.absys.saas.tenant.platform.notification.application.service.NotificationCommandService;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationChannel;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmedEventHandler {

    private final NotificationCommandService notificationCommandService;

    public OrderConfirmedEventHandler(NotificationCommandService notificationCommandService) {
        this.notificationCommandService = notificationCommandService;
    }

    @EventListener
    public void handle(OrderConfirmedEvent event) {

        notificationCommandService.createForTenant(event.tenantId(), new CreateNotificationCommand(NotificationChannel.IN_APP, event.customerId().toString(), "Order confirmed", "Your order " + event.orderId() + " has been confirmed. " + "Total amount: " + event.totalAmount()));
    }
}