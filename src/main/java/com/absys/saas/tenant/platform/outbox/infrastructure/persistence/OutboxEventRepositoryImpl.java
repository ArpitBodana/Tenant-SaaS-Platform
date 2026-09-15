package com.absys.saas.tenant.platform.outbox.infrastructure.persistence;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventStatus;
import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public List<OutboxEventId> findPendingIds(int limit) {

        return repository.findPendingIds(limit).stream().map(OutboxEventId::of).toList();
    }

    @Override
    public Optional<OutboxEvent> findPendingById(OutboxEventId eventId) {

        return repository.findPendingByIdForUpdate(eventId.value()).map(this::toDomain);
    }

    @Override
    public void update(OutboxEvent event) {

        OutboxEventJpaEntity entity = repository.findById(event.id().value()).orElseThrow(() -> new IllegalStateException("Outbox event not found: " + event.id().value()));

        if (event.status() == OutboxEventStatus.PROCESSED) {
            entity.markProcessed(event.processedAt());
        } else {
            entity.registerFailure(event.status(), event.retryCount(), event.nextAttemptAt());
        }

        repository.save(entity);
    }

    private OutboxEventJpaEntity toEntity(OutboxEvent event) {
        return new OutboxEventJpaEntity(event.id().value(), event.eventType(), event.aggregateType(), event.aggregateId(), event.payload(), event.createdAt(), event.processedAt(), event.retryCount(), event.status(), event.nextAttemptAt());
    }

    private OutboxEvent toDomain(OutboxEventJpaEntity entity) {
        return OutboxEvent.restore(OutboxEventId.of(entity.getId()), entity.getEventType(), entity.getAggregateType(), entity.getAggregateId(), entity.getPayload(), entity.getCreatedAt(), entity.getStatus(), entity.getProcessedAt(), entity.getRetryCount(), entity.getNextAttemptAt());
    }


}