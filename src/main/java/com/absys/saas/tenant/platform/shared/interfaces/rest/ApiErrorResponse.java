package com.absys.saas.tenant.platform.shared.interfaces.rest;

import java.time.Instant;

public record ApiErrorResponse(
        String code,
        String message,
        Instant timestamp
) {
}