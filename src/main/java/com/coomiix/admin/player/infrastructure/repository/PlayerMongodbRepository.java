package com.coomiix.admin.player.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.PlayerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlayerMongodbRepository implements PlayerRepository {

    private final MongoTemplate mongoTemplate;
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
    public boolean existsById(String id) {
        log.info("Checking existence of player by ID in MongoDB: {}", id);
        return mongoRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting player by ID in MongoDB: {}", id);
        mongoRepository.deleteById(id);
        log.info("Player with ID {} deleted successfully from MongoDB", id);
    }

    @Override
    public Page<Player> search(String name, String email, String classType, Pageable pageable) {
        log.info("Searching players in MongoDB with name: {}, email: {}, classType: {}, pageable: {}", name, email, classType, pageable);
        Query query = new Query().with(pageable);

        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (email != null && !email.isEmpty()) {
            query.addCriteria(Criteria.where("email.value").is(email));
        }
        if (classType != null && !classType.isEmpty()) {
            query.addCriteria(Criteria.where("classType").is(classType));
        }

        List<PlayerDocument> documents = mongoTemplate.find(query, PlayerDocument.class);
        long total = mongoTemplate.count(query.skip(-1).limit(-1), PlayerDocument.class);
        Page<PlayerDocument> page = new PageImpl<>(documents, pageable, total);

        log.info("Found {} players matching criteria in MongoDB", page.getTotalElements());
        return page.map(PlayerDocumentMapper.INSTANCE::toPlayer);
    }

}
