package com.absys.saas.tenant.platform.audit.application.command;

import com.absys.saas.tenant.platform.audit.domain.model.AuditAction;
import com.absys.saas.tenant.platform.audit.domain.model.AuditActorType;

import java.util.UUID;

public record RecordAuditCommand(
        UUID tenantId,
        UUID actorId,
        AuditActorType actorType,
        AuditAction action,
        String aggregateType,
        String aggregateId,
        String description
) {
}