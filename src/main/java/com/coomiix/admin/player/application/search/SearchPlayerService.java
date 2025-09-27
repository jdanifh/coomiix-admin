package com.coomiix.admin.player.application.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class SearchPlayerService {

    private final PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Player findById(String id) {
        log.info("Searching for player with ID: {}", id);
        return playerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Page<Player> searchPlayers(SearchPlayerQuery query, Pageable pageable) {
        log.info("Searching for players with query: {} and pageable: {}", query, pageable);
        return playerRepository.search(query.name(), query.email(), query.classType(), pageable);

    }

}
