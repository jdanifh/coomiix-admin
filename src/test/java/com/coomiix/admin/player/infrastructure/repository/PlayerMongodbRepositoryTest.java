package com.coomiix.admin.player.infrastructure.repository;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Test
    void shouldFindPlayerById() {
        Player player = Player.create("Test User", "test@example.com", "Warrior");
        Player saved = playerMongodbRepository.save(player);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertTrue(playerMongodbRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void shouldNotFindNonExistentPlayerById() {
        assertFalse(playerMongodbRepository.findById("non-existent-id").isPresent());
    }

    @Test
    void sholdCheckPlayerExistsById() {
        Player player = Player.create("Test User", "test@example.com", "Warrior");
        Player saved = playerMongodbRepository.save(player);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertTrue(playerMongodbRepository.existsById(saved.getId()));
    }

    @Test
    void shouldCheckPlayerDoesNotExistById() {
        assertFalse(playerMongodbRepository.existsById("non-existent-id"));
    }

    @Test
    void shouldDeletePlayerById() {
        Player player = Player.create("Test User", "test@example.com", "Warrior");
        Player saved = playerMongodbRepository.save(player);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        playerMongodbRepository.deleteById(saved.getId());
        assertFalse(playerMongodbRepository.existsById(saved.getId()));
    }

    @Test
    void shouldSearchPlayersByName() {
        Player player = Player.create("Alice", "alice@example.com", "Mage");
        playerMongodbRepository.save(player);
        Page<Player> result = playerMongodbRepository.search("Ali", null, null, PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Alice", result.getContent().get(0).getName());
    }

    @Test
    void shouldSearchPlayersByEmail() {
        Player player = Player.create("Bob", "bob@example.com", "Warrior");
        playerMongodbRepository.save(player);
        Page<Player> result = playerMongodbRepository.search(null, "bob@example.com", null, PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Bob", result.getContent().get(0).getName());
    }

    @Test
    void shouldSearchPlayersByClassType() {
        Player player = Player.create("Charlie", "charlie@example.com", "Rogue");
        playerMongodbRepository.save(player);
        Page<Player> result = playerMongodbRepository.search(null, null, "Rogue", PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Charlie", result.getContent().get(0).getName());
    }

    @Test
    void shouldNotFindPlayersWithNonMatchingCriteria() {
        Page<Player> result = playerMongodbRepository.search("NonExistent", null, null, PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

}
