package com.absys.saas.tenant.platform.customer.application.dto;

import com.absys.saas.tenant.platform.customer.domain.model.CustomerStatus;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email,
        String phone,
        CustomerStatus status
) {
}