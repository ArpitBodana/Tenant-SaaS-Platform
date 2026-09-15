package com.absys.saas.tenant.platform.outbox.application.service;

import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEvent;
import com.absys.saas.tenant.platform.outbox.domain.model.OutboxEventId;
import com.absys.saas.tenant.platform.outbox.domain.repository.OutboxEventRepository;
import com.absys.saas.tenant.platform.shared.application.event.OrderConfirmedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class OutboxEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventProcessor.class);

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    private final Counter processedCounter;
    private final Counter failedCounter;
    private final Counter retryCounter;
    private final Timer processingTimer;

    public OutboxEventProcessor(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper, ApplicationEventPublisher eventPublisher, MeterRegistry meterRegistry) {

        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.eventPublisher = eventPublisher;

        this.processedCounter = Counter.builder("saas.outbox.processed").description("Successfully processed outbox events").register(meterRegistry);

        this.failedCounter = Counter.builder("saas.outbox.failed").description("Failed outbox events").register(meterRegistry);

        this.retryCounter = Counter.builder("saas.outbox.retry").description("Outbox event retries").register(meterRegistry);

        this.processingTimer = Timer.builder("saas.outbox.processing.time").description("Outbox event processing duration").register(meterRegistry);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(OutboxEventId eventId) {

        Timer.Sample sample = Timer.start();

        OutboxEvent event = outboxEventRepository.findPendingById(eventId).orElse(null);

        if (event == null) {
            return;
        }

        try {

            log.info("Processing outbox event. eventId={}, eventType={}, retryCount={}", event.id().value(), event.eventType(), event.retryCount());

            if (event.eventType().equals(OrderConfirmedEvent.class.getName())) {

                OrderConfirmedEvent orderConfirmedEvent = objectMapper.readValue(event.payload(), OrderConfirmedEvent.class);

                eventPublisher.publishEvent(orderConfirmedEvent);
            }

            event.markProcessed();
            outboxEventRepository.update(event);

            processedCounter.increment();

            log.info("Outbox event processed successfully. eventId={}", event.id().value());

        } catch (Exception exception) {

            event.registerFailure();
            outboxEventRepository.update(event);

            failedCounter.increment();
            retryCounter.increment();

            log.error("Outbox event processing failed. eventId={}, retryCount={}, nextAttemptAt={}", event.id().value(), event.retryCount(), event.nextAttemptAt(), exception);

        } finally {

            sample.stop(processingTimer);
        }
    }
}