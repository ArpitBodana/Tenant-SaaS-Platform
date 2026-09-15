package com.absys.saas.tenant.platform.outbox.application.service;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;
import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    public void store(OrderConfirmedEvent event) {

        try {

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.create(OutboxEventId.generate(), OrderConfirmedEvent.class.getName(), "Order", event.orderId().toString(), payload);

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException exception) {

            throw new IllegalStateException("Unable to serialize order confirmed event", exception);
        }
    }
}