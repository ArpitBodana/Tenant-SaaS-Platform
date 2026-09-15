package com.absys.saas.tenant.platform.outbox.application.service;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OutboxPublisher(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publish() {

        var events = outboxEventRepository.findUnprocessed(50);

        for (OutboxEvent event : events) {

            try {

                if (event.eventType().equals(OrderConfirmedEvent.class.getName())) {

                    OrderConfirmedEvent orderConfirmedEvent = objectMapper.readValue(event.payload(), OrderConfirmedEvent.class);

                    eventPublisher.publishEvent(orderConfirmedEvent);
                }

                event.markProcessed();

                outboxEventRepository.save(event);

            } catch (Exception exception) {

                event.incrementRetryCount();

                outboxEventRepository.save(event);
            }
        }
    }
}