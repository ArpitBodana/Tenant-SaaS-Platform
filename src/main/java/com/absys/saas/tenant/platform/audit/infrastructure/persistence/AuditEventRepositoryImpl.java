package com.absys.saas.tenant.platform.audit.infrastructure.persistence;

import com.absys.saas.tenant.platform.audit.domain.model.AuditEvent;
import com.absys.saas.tenant.platform.audit.domain.model.AuditEventId;
import com.absys.saas.tenant.platform.audit.domain.repository.AuditEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AuditEventRepositoryImpl
        implements AuditEventRepository {

    private final SpringDataAuditEventRepository repository;

    public AuditEventRepositoryImpl(
            SpringDataAuditEventRepository repository) {

        this.repository = repository;
    }

    @Override
    public AuditEvent save(AuditEvent event) {

        AuditEventJpaEntity entity = new AuditEventJpaEntity(
                event.id().value(),
                event.tenantId(),
                event.actorId(),
                event.actorType(),
                event.action(),
                event.aggregateType(),
                event.aggregateId(),
                event.description(),
                event.occurredAt()
        );

        AuditEventJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public List<AuditEvent> findByTenantId(UUID tenantId) {

        return repository
                .findByTenantIdOrderByOccurredAtDesc(tenantId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<AuditEvent> findByAggregate(
            String aggregateType,
            String aggregateId) {

        return repository
                .findByAggregateTypeAndAggregateIdOrderByOccurredAtDesc(
                        aggregateType,
                        aggregateId
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private AuditEvent toDomain(AuditEventJpaEntity entity) {

        return AuditEvent.restore(
                AuditEventId.of(entity.getId()),
                entity.getTenantId(),
                entity.getActorId(),
                entity.getActorType(),
                entity.getAction(),
                entity.getAggregateType(),
                entity.getAggregateId(),
                entity.getDescription(),
                entity.getOccurredAt()
        );
    }
}