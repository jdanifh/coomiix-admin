package com.coomiix.admin.player.infrastructure.repository;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import(PlayerMongodbRepository.class)
class PlayerMongodbRepositoryTest extends TestContainerTest {
    @Autowired
    private PlayerMongodbRepository playerMongodbRepository;

    @Test
    void shouldSavePlayerToMongoDb() {
        Player player = Player.create("Test User", "test@example.com", "Warrior");
        Player saved = playerMongodbRepository.save(player);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(player.getName(), saved.getName());
        assertEquals(player.getEmail(), saved.getEmail());
        assertEquals(player.getClassType(), saved.getClassType());
        assertEquals(1, saved.getLevel());
    }
}
