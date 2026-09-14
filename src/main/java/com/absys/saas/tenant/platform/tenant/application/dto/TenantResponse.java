package com.absys.saas.tenant.platform.tenant.application.dto;

import java.util.UUID;

public record TenantResponse(
        UUID id,
        String name,
        String status
) {
}