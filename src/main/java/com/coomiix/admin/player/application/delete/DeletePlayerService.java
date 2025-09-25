package com.coomiix.admin.player.application.delete;

import org.springframework.stereotype.Service;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.PlayerRepository;
import com.coomiix.admin.player.domain.events.PlayerDeletedEvent;
import com.coomiix.admin.shared.domain.events.EventPublisher;
import com.coomiix.admin.shared.domain.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeletePlayerService {

    private final PlayerRepository playerRepository;
    private final EventPublisher eventPublisher;

    public void deleteById(String id) {
        log.info("Deleting player with ID: {}", id);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with ID: " + id));
        
        playerRepository.deleteById(id);
        log.info("Player with ID {} deleted successfully", id);
        PlayerDeletedEvent event = new PlayerDeletedEvent(id, player.getName(), player.getEmail().value());
        log.info("Publishing PlayerDeletedEvent: {}", event);
        eventPublisher.publish(event);
    }

}
