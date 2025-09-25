package com.coomiix.admin.player.application.delete;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coomiix.admin.player.application.create.CreatePlayerCommand;
import com.coomiix.admin.player.application.create.CreatePlayerService;
import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.events.PlayerDeletedEvent;
import com.coomiix.admin.shared.domain.exceptions.ResourceNotFoundException;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

@SpringBootTest
public class DeletePlayerServiceTest extends TestContainerTest {

    @Autowired
    private CreatePlayerService createPlayerService;
    @Autowired
    private DeletePlayerService deletePlayerService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldDeletePlayerSuccessfully() {
        CreatePlayerCommand createCommand = new CreatePlayerCommand("Jane Doe", "email@example.com", "Archer");
        Player saved = createPlayerService.create(createCommand);
        assertNotNull(saved);

        deletePlayerService.deleteById(saved.getId());
        Object message = rabbitTemplate.receiveAndConvert(PlayerDeletedEvent.EVENT_NAME);
        assertNotNull(message);

        assertThrows(ResourceNotFoundException.class, () -> {
            deletePlayerService.deleteById(saved.getId());
        });
    }

    @Test
    void shouldThrowExceptionForNonExistentPlayer() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deletePlayerService.deleteById("non-existent-id");
        });
        assertTrue(exception.getMessage().contains("Player not found with ID: non-existent-id"));
    }

}
