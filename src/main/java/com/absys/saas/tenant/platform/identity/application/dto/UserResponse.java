package com.absys.saas.tenant.platform.identity.application.dto;

import com.absys.saas.tenant.platform.identity.domain.model.UserRole;
import com.absys.saas.tenant.platform.identity.domain.model.UserStatus;

import java.util.UUID;

public record UserResponse(UUID id, UUID tenantId, String email, UserRole role, UserStatus status) {
}