package com.coomiix.admin.player.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.coomiix.admin.api.PlayersApi;
import com.coomiix.admin.model.PlayerPage;
import com.coomiix.admin.model.PlayerRequest;
import com.coomiix.admin.model.PlayerResponse;
import com.coomiix.admin.player.application.create.CreatePlayerCommand;
import com.coomiix.admin.player.application.create.CreatePlayerService;
import com.coomiix.admin.player.application.delete.DeletePlayerService;
import com.coomiix.admin.player.application.update.UpdatePlayerCommand;
import com.coomiix.admin.player.application.update.UpdatePlayerService;
import com.coomiix.admin.player.domain.Player;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlayerRestController implements PlayersApi {

    private final CreatePlayerService createPlayerService;
    private final UpdatePlayerService updatePlayerService;
    private final DeletePlayerService deletePlayerService;

    @Override
    public ResponseEntity<PlayerResponse> createPlayer(@Valid PlayerRequest playerRequest) {
        log.info("Received request to create player: {}", playerRequest);
        CreatePlayerCommand command = CreatePlayerCommand.of(playerRequest);
        Player newPlayer = createPlayerService.create(command);
        log.info("Player created successfully: {}", newPlayer);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponseMapper.INSTANCE.toPlayerResponse(newPlayer));
    }

    @Override
    public ResponseEntity<Void> deletePlayer(String id) {
        log.info("Received request to delete player with ID: {}", id);
        deletePlayerService.deleteById(id);
        log.info("Player with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PlayerResponse> getPlayerById(String id) {
        // TODO Auto-generated method stub
        return PlayersApi.super.getPlayerById(id);
    }

    @Override
    public ResponseEntity<PlayerPage> searchPlayers(@Min(0) @Valid Integer page, @Min(1) @Max(100) @Valid Integer size,
            @Valid String sort, @Valid String name, @Valid String email, @Valid String classType) {
        // TODO Auto-generated method stub
        return PlayersApi.super.searchPlayers(page, size, sort, name, email, classType);
    }

    @Override
    public ResponseEntity<PlayerResponse> updatePlayer(String id, @Valid PlayerRequest playerRequest) {
        log.info("Received request to update player with ID {}: {}", id, playerRequest);
        UpdatePlayerCommand command = UpdatePlayerCommand.of(id, playerRequest);
        Player updated = updatePlayerService.update(command);
        log.info("Player updated successfully: {}", updated);
        return ResponseEntity.ok(PlayerResponseMapper.INSTANCE.toPlayerResponse(updated));
    }

}
