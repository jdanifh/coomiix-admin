package com.coomiix.admin.shared.domain.events;

public interface EventPublisher {

    public void publish(DomainEvent event);

}
