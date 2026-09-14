package com.absys.saas.tenant.platform.customer.application.command;

public record CreateCustomerCommand(
        String name,
        String email,
        String phone
) {
}