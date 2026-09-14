package com.absys.saas.tenant.platform.identity.application.command;

import java.util.UUID;

public record ActivateUserCommand(UUID userId) {
}