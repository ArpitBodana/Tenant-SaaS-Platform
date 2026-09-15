package com.absys.saas.tenant.platform.outbox.infrastructure.persistence;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;
import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxEventRepositoryImpl implements OutboxEventRepository {

    private final SpringDataOutboxEventRepository repository;

    public OutboxEventRepositoryImpl(SpringDataOutboxEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public OutboxEvent save(OutboxEvent event) {

        OutboxEventJpaEntity saved = repository.save(toEntity(event));

        return toDomain(saved);
    }

    @Override
    public OutboxEvent saveAndFlush(OutboxEvent event) {

        OutboxEventJpaEntity saved = repository.saveAndFlush(toEntity(event));

        return toDomain(saved);
    }

    @Override
    public List<OutboxEvent> findUnprocessed(int limit) {

        return repository.findUnprocessed().stream().limit(limit).map(this::toDomain).toList();
    }

    private OutboxEventJpaEntity toEntity(OutboxEvent event) {

        return new OutboxEventJpaEntity(event.id().value(), event.eventType(), event.aggregateType(), event.aggregateId(), event.payload(), event.createdAt(), event.processedAt(), event.retryCount());
    }

    private OutboxEvent toDomain(OutboxEventJpaEntity entity) {

        return OutboxEvent.restore(OutboxEventId.of(entity.getId()), entity.getEventType(), entity.getAggregateType(), entity.getAggregateId(), entity.getPayload(), entity.getCreatedAt(), entity.getProcessedAt(), entity.getRetryCount());
    }
}