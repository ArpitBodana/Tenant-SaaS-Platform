package com.absys.saas.tenant.platform.outbox.domain.repository;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository {

    OutboxEvent save(OutboxEvent event);

    List<OutboxEventId> findPendingIds(int limit);

    Optional<OutboxEvent> findPendingById(OutboxEventId eventId);

    void update(OutboxEvent event);
}