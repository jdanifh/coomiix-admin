package com.coomiix.admin.player.domain.events;

import com.coomiix.admin.shared.domain.events.DomainEvent;

import lombok.Getter;

@Getter
public class PlayerDeletedEvent extends DomainEvent {

    public static final String EVENT_NAME = "player.deleted";

    private final String playerId;
    private final String playerName;
    private final String playerEmail;

    public PlayerDeletedEvent(String playerId, String playerName, String playerEmail) {
        super(EVENT_NAME);
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerEmail = playerEmail;
    }

}
