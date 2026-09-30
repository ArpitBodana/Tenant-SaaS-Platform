package com.absys.saas.tenant.platform.audit.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataAuditEventRepository
        extends JpaRepository<AuditEventJpaEntity, UUID> {

    List<AuditEventJpaEntity> findByTenantIdOrderByOccurredAtDesc(
            UUID tenantId
    );

    List<AuditEventJpaEntity> findByAggregateTypeAndAggregateIdOrderByOccurredAtDesc(
            String aggregateType,
            String aggregateId
    );
}