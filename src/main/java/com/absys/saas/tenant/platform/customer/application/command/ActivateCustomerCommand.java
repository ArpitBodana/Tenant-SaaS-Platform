package com.absys.saas.tenant.platform.customer.application.command;

import java.util.UUID;

public record ActivateCustomerCommand(
        UUID customerId
) {
}