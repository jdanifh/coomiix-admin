package com.coomiix.admin.player.application.update;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.PlayerRepository;
import com.coomiix.admin.shared.domain.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePlayerService {

    private final PlayerRepository playerRepository;

    @Transactional
    public Player update(UpdatePlayerCommand command) {
        log.info("Starting player update process for ID: {}", command.id());
        Player player = playerRepository.findById(command.id())
            .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + command.id() + " not found"));

        player.update(command.name(), command.email(), command.classType());
        Player updated = playerRepository.save(player);
        log.info("Player updated successfully: {}", updated);
        return updated;
    }

}
