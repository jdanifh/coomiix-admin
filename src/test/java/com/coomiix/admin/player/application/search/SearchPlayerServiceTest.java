package com.coomiix.admin.player.application.search;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.coomiix.admin.player.application.create.CreatePlayerCommand;
import com.coomiix.admin.player.application.create.CreatePlayerService;
import com.coomiix.admin.player.application.delete.DeletePlayerService;
import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.shared.domain.exceptions.ResourceNotFoundException;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

@SpringBootTest
public class SearchPlayerServiceTest extends TestContainerTest {

    @Autowired
    private CreatePlayerService createPlayerService;
    @Autowired
    private DeletePlayerService deletePlayerService;
    @Autowired
    private SearchPlayerService searchPlayerService;

    private String playerId;

    @BeforeEach
    void setUp() {
        CreatePlayerCommand createCommand = new CreatePlayerCommand("John Doe", "john@example.com", "Warrior");
        Player saved = createPlayerService.create(createCommand);
        this.playerId = saved.getId();
    }

    @AfterEach
    void tearDown() {
        if (playerId != null) {
            deletePlayerService.deleteById(playerId);
        }
    }

    @Test
    void shouldFindPlayerById() {
        Player result = searchPlayerService.findById(playerId);
        assertNotNull(result);
        assertEquals(playerId, result.getId());
    }

    @Test
    void shouldThrowExceptionForNonExistentPlayer() {
        String nonExistentId = "non-existent-id";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            searchPlayerService.findById(nonExistentId);
        });
        assertTrue(exception.getMessage().contains("Player with ID " + nonExistentId + " not found"));
    }

    @Test
    void shouldSearchPlayersByName() {
        SearchPlayerQuery query = new SearchPlayerQuery("John", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Player> result = searchPlayerService.searchPlayers(query, pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
    }

    @Test
    void shouldReturnEmptyWhenNoMatch() {
        SearchPlayerQuery query = new SearchPlayerQuery("NonExistentName", null, null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Player> result = searchPlayerService.searchPlayers(query, pageable);
        assertNotNull(result);
        assertFalse(result.hasContent());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void shouldSearchPlayersByEmail() {
        SearchPlayerQuery query = new SearchPlayerQuery(null, "john@example.com", null);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Player> result = searchPlayerService.searchPlayers(query, pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getTotalElements());
        assertEquals("john@example.com", result.getContent().get(0).getEmail().value());
    }

    @Test
    void shouldSearchPlayersByClassType() {
        SearchPlayerQuery query = new SearchPlayerQuery(null, null, "Warrior");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Player> result = searchPlayerService.searchPlayers(query, pageable);
        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(1, result.getTotalElements());
        assertEquals("Warrior", result.getContent().get(0).getClassType());
    }

}
