package com.coomiix.admin.shared.infrastructure.messaging;

import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.coomiix.admin.shared.domain.events.DomainEvent;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplateCustomizer rabbitTemplateCustomizer(Jackson2JsonMessageConverter converter) {
        return template -> template.setMessageConverter(converter);
    }

    @Bean
    public Exchange domainExchange() {
        return ExchangeBuilder.topicExchange("coomiix.admin").build();
    }

    @Bean
    public Declarables domainBindings() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<Queue> queues = new ArrayList<>();
        ArrayList<Binding> bindings = new ArrayList<>();

        for (String event : getEvents()) {
            Queue q = QueueBuilder.durable(event).build();
            queues.add(q);
            Binding bind = BindingBuilder.bind(q).to(domainExchange()).with(event).noargs();
            bindings.add(bind);
        }
        
        ArrayList<Declarable> all = new ArrayList<>();
        all.add(domainExchange());
        all.addAll(queues);
        all.addAll(bindings);
        return new Declarables(all);
    }

    private List<String> getEvents() throws NoSuchFieldException, IllegalAccessException {
        List<String> events = new ArrayList<>();
        Reflections reflections = new Reflections("com.coomiix.admin");

        for (Class<? extends DomainEvent> eventClass : reflections.getSubTypesOf(DomainEvent.class)) {
            events.add(eventClass.getDeclaredField("EVENT_NAME").get(null).toString());
        }

        return events;
    }

}
