package com.coomiix.admin.shared.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public abstract class DomainEvent {

    private final UUID id = UUID.randomUUID();
    private final String eventName;
    private final LocalDateTime occurredOn = LocalDateTime.now();

}
