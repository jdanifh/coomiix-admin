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
        // TODO Auto-generated method stub
        return PlayersApi.super.deletePlayer(id);
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
        // TODO Auto-generated method stub
        return PlayersApi.super.updatePlayer(id, playerRequest);
    }

}
