package com.coomiix.admin.player.application.create;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.PlayerRepository;
import com.coomiix.admin.player.domain.events.PlayerCreatedEvent;
import com.coomiix.admin.shared.domain.events.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePlayerService {

    private final PlayerRepository playerRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Player create(CreatePlayerCommand command) {
        log.info("Starting player creation process for: {}", command);
        Player newPlayer = Player.create(command.name(), command.email(), command.classType());
        Player saved = this.playerRepository.save(newPlayer);
        log.info("Player created successfully: {}", saved);
        PlayerCreatedEvent event = new PlayerCreatedEvent(saved.getId(), saved.getName(), saved.getCreatedAt());
        log.info("Publishing PlayerCreatedEvent: {}", event);
        this.eventPublisher.publish(event);
        return saved;

    }

}
