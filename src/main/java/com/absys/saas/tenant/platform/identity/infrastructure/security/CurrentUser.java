package com.absys.saas.tenant.platform.identity.infrastructure.security;

import com.absys.saas.tenant.platform.identity.domain.model.UserRole;

import java.util.UUID;

public record CurrentUser(
        UUID userId,
        UUID tenantId,
        UserRole role
) {
}