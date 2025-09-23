package com.coomiix.admin.player.application.create;

import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.player.domain.events.PlayerCreatedEvent;
import com.coomiix.admin.shared.domain.exceptions.ValueNotValidException;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreatePlayerServiceTest extends TestContainerTest {

    @Autowired
    private CreatePlayerService createPlayerService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldCreatePlayerSuccessfully() {
        CreatePlayerCommand command = new CreatePlayerCommand("John Doe", "john@example.com", "Warrior");
        Player result = createPlayerService.create(command);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(command.name(), result.getName());
        assertEquals(command.email(), result.getEmail().value());
        assertEquals(command.classType(), result.getClassType());
        assertEquals(1, result.getLevel());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());

        Object message = rabbitTemplate.receiveAndConvert(PlayerCreatedEvent.EVENT_NAME);
        assertNotNull(message);
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        CreatePlayerCommand command = new CreatePlayerCommand("Jane Doe", "invalid-email", "Mage");
        ValueNotValidException exception = assertThrows(ValueNotValidException.class, () -> {
            createPlayerService.create(command);
        });
        assertTrue(exception.getMessage().contains("Invalid email address"));
    }

    @Test
    void shouldThrowExceptionForEmptyEmail() {
        CreatePlayerCommand command = new CreatePlayerCommand("Empty Email", "", "Mage");
        ValueNotValidException exception = assertThrows(ValueNotValidException.class, () -> {
            createPlayerService.create(command);
        });
        assertTrue(exception.getMessage().contains("Email cannot be null or empty"));
    }
}
