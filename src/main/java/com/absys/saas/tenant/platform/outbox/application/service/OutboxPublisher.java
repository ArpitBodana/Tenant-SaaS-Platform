package com.absys.saas.tenant.platform.outbox.application.service;

import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    public OutboxPublisher(OutboxEventRepository outboxEventRepository, OutboxEventProcessor outboxEventProcessor) {

        this.outboxEventRepository = outboxEventRepository;
        this.outboxEventProcessor = outboxEventProcessor;
    }

    @Scheduled(fixedDelay = 5000)
    public void publish() {

        var eventIds = outboxEventRepository.findPendingIds(50);

        if (eventIds.isEmpty()) {
            return;
        }

        log.info("Outbox publisher found {} pending events", eventIds.size());

        for (var eventId : eventIds) {
            outboxEventProcessor.process(eventId);
        }
    }
}