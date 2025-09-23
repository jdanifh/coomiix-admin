package com.coomiix.admin.shared.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.coomiix.admin.shared.domain.events.DomainEvent;
import com.coomiix.admin.shared.domain.events.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(DomainEvent event) {
        rabbitTemplate.convertAndSend(event.getEventName(), event);
        log.info("Event published successfully to RabbitMQ: {}", event);
    }

}
