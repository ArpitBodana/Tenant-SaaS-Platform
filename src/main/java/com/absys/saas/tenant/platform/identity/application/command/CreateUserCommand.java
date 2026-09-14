package com.absys.saas.tenant.platform.identity.application.command;

import com.absys.saas.tenant.platform.identity.domain.model.UserRole;

import java.util.UUID;

public record CreateUserCommand(UUID tenantId, String email, String password, UserRole role) {
}