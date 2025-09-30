package com.coomiix.admin.player.infrastructure.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.coomiix.admin.api.PlayersApi;
import com.coomiix.admin.model.PlayerPage;
import com.coomiix.admin.model.PlayerRequest;
import com.coomiix.admin.model.PlayerResponse;
import com.coomiix.admin.player.application.create.CreatePlayerCommand;
import com.coomiix.admin.player.application.create.CreatePlayerService;
import com.coomiix.admin.player.application.delete.DeletePlayerService;
import com.coomiix.admin.player.application.search.SearchPlayerQuery;
import com.coomiix.admin.player.application.search.SearchPlayerService;
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
    private final SearchPlayerService searchPlayerService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerResponse> createPlayer(@Valid PlayerRequest playerRequest) {
        log.info("Received request to create player: {}", playerRequest);
        CreatePlayerCommand command = CreatePlayerCommand.of(playerRequest);
        Player newPlayer = createPlayerService.create(command);
        log.info("Player created successfully: {}", newPlayer);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponseMapper.INSTANCE.toPlayerResponse(newPlayer));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlayer(String id) {
        log.info("Received request to delete player with ID: {}", id);
        deletePlayerService.deleteById(id);
        log.info("Player with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerResponse> getPlayerById(String id) {
        log.info("Received request to get player with ID: {}", id);
        Player player = searchPlayerService.findById(id);
        log.info("Player found: {}", player);
        return ResponseEntity.ok(PlayerResponseMapper.INSTANCE.toPlayerResponse(player));
    }

    @Override
    public ResponseEntity<PlayerPage> searchPlayers(@Min(0) @Valid Integer page, @Min(1) @Max(100) @Valid Integer size,
            @Valid String sort, @Valid String name, @Valid String email, @Valid String classType) {
        log.info("Received request to search players with page: {}, size: {}, sort: {}, name: {}, email: {}, classType: {}",
                 page, size, sort, name, email, classType);
        SearchPlayerQuery query = new SearchPlayerQuery(name, email, classType);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Player> players = searchPlayerService.searchPlayers(query, pageable);
        log.info("Players found: {}", players.getTotalElements());
        PlayerPage playerPage = PlayerPageMapper.INSTANCE.toPlayerPage(players);
        return ResponseEntity.ok(playerPage);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerResponse> updatePlayer(String id, @Valid PlayerRequest playerRequest) {
        log.info("Received request to update player with ID {}: {}", id, playerRequest);
        UpdatePlayerCommand command = UpdatePlayerCommand.of(id, playerRequest);
        Player updated = updatePlayerService.update(command);
        log.info("Player updated successfully: {}", updated);
        return ResponseEntity.ok(PlayerResponseMapper.INSTANCE.toPlayerResponse(updated));
    }

}
