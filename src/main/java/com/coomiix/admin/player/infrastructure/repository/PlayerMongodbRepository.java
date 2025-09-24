package com.coomiix.admin.player.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.PlayerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlayerMongodbRepository implements PlayerRepository {

    private final PlayerDocumentRepository mongoRepository;

    @Override
    public Player save(Player player) {
        log.info("Saving player to MongoDB: {}", player);
        PlayerDocument document = PlayerDocumentMapper.INSTANCE.toPlayerDocument(player);
        PlayerDocument saved = mongoRepository.save(document);
        log.info("Player saved successfully: {}", saved);
        return PlayerDocumentMapper.INSTANCE.toPlayer(saved);
    }

    @Override
    public Optional<Player> findById(String id) {
        log.info("Finding player by ID in MongoDB: {}", id);
        return mongoRepository.findById(id).map(PlayerDocumentMapper.INSTANCE::toPlayer);
    }

    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
