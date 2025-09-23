package com.coomiix.admin.player.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerDocumentRepository extends MongoRepository<PlayerDocument, String> {

}
