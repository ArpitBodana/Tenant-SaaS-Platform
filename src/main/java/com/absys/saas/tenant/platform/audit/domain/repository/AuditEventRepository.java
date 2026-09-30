package com.absys.saas.tenant.platform.audit.domain.repository;

import com.absys.saas.tenant.platform.audit.domain.model.AuditEvent;

import java.util.List;
import java.util.UUID;

public interface AuditEventRepository {

    AuditEvent save(AuditEvent event);

    List<AuditEvent> findByTenantId(UUID tenantId);

    List<AuditEvent> findByAggregate(
            String aggregateType,
            String aggregateId
    );
}