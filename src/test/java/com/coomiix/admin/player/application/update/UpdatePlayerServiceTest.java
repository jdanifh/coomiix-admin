package com.coomiix.admin.player.application.update;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coomiix.admin.player.application.create.CreatePlayerCommand;
import com.coomiix.admin.player.application.create.CreatePlayerService;
import com.coomiix.admin.player.domain.Player;
import com.coomiix.admin.shared.domain.exceptions.ResourceNotFoundException;
import com.coomiix.admin.shared.domain.exceptions.ValueNotValidException;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;

@SpringBootTest
class UpdatePlayerServiceTest extends TestContainerTest {

    @Autowired
    private CreatePlayerService createPlayerService;
    @Autowired
    private UpdatePlayerService updatePlayerService;

    @Test
    void shouldUpdatePlayerSuccessfully() {
        CreatePlayerCommand createCommand = new CreatePlayerCommand("John Doe", "john@example.com", "Warrior");
        Player saved = createPlayerService.create(createCommand);

        UpdatePlayerCommand command = new UpdatePlayerCommand(saved.getId(), "John Updated", "new.mail@example.com", "Mage");
        Player result = updatePlayerService.update(command);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(command.name(), result.getName());
        assertEquals(command.email(), result.getEmail().value());
        assertEquals(command.classType(), result.getClassType());
        assertEquals(1, result.getLevel());
        assertNotNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());

    }

    @Test
    void shouldThrowExceptionForNonExistentPlayer() {
        UpdatePlayerCommand command = new UpdatePlayerCommand("non-existent-id", "Name", "name@example.com", "ClassType");
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            updatePlayerService.update(command);
        });
        assertTrue(exception.getMessage().contains("Player with ID non-existent-id not found"));
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        CreatePlayerCommand createCommand = new CreatePlayerCommand("John Doe", "john@example.com", "Warrior");
        Player saved = createPlayerService.create(createCommand);

        UpdatePlayerCommand command = new UpdatePlayerCommand(saved.getId(), "John Updated", "invalid-email", "Mage");
        ValueNotValidException exception = assertThrows(ValueNotValidException.class, () -> {
            updatePlayerService.update(command);
        });
        assertTrue(exception.getMessage().contains("Invalid email address"));
    }

    @Test
    void shouldThrowExceptionForEmptyEmail() {
        CreatePlayerCommand createCommand = new CreatePlayerCommand("John Doe", "john@example.com", "Warrior");
        Player saved = createPlayerService.create(createCommand);

        UpdatePlayerCommand command = new UpdatePlayerCommand(saved.getId(), "John Updated", "", "Mage");
        ValueNotValidException exception = assertThrows(ValueNotValidException.class, () -> {
            updatePlayerService.update(command);
        });
        assertTrue(exception.getMessage().contains("Email cannot be null or empty"));
    }
}
