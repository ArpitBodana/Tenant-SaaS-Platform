package com.absys.saas.tenant.platform.identity.application.authentication;

import java.util.UUID;

public record AuthenticationResponse(
        String accessToken,
        String tokenType,
        UUID userId,
        UUID tenantId,
        String role
) {
}