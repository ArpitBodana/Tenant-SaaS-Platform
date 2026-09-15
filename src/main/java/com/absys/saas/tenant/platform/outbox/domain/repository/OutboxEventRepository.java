package com.absys.saas.tenant.platform.outbox.domain.repository;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;

import java.util.List;

public interface OutboxEventRepository {

    OutboxEvent save(OutboxEvent event);

    List<OutboxEvent> findUnprocessed(int limit);

    OutboxEvent saveAndFlush(OutboxEvent event);
}