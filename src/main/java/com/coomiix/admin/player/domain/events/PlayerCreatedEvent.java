package com.coomiix.admin.player.domain.events;

import java.time.LocalDateTime;

import com.coomiix.admin.shared.domain.events.DomainEvent;

import lombok.Getter;

@Getter
public class PlayerCreatedEvent extends DomainEvent {

    public static final String EVENT_NAME = "player.created";

    private final String playerId;
    private final String playerName;
    private final LocalDateTime createdAt;

    public PlayerCreatedEvent(String playerId, String playerName, LocalDateTime createdAt) {
        super(EVENT_NAME);
        this.playerId = playerId;
        this.playerName = playerName;
        this.createdAt = createdAt;
    }

}
