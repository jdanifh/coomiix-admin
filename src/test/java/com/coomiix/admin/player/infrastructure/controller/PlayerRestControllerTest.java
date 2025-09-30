package com.coomiix.admin.player.infrastructure.controller;

import com.coomiix.admin.model.PlayerRequest;
import com.coomiix.admin.shared.infrastructure.TestContainerTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerRestControllerTest extends TestContainerTest {

    private static final String PLAYERS_URL = "/api/v1/players";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreatePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.class_type").value(request.getClassType()))
                .andExpect(jsonPath("$.level").value("1"))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Invalid Email");
        request.setEmail("not-an-email");
        request.setClassType("Mage");

        mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALUE_NOT_VALID"))
                .andExpect(jsonPath("$.message").value("Invalid email address"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequestForEmptyEmail() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Empty Email");
        request.setEmail("");
        request.setClassType("Mage");

        mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALUE_NOT_VALID"))
                .andExpect(jsonPath("$.message").value("Email cannot be null or empty"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdatePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        String responseContent = mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String playerId = objectMapper.readTree(responseContent).get("id").asText();
        PlayerRequest updateRequest = new PlayerRequest();
        updateRequest.setName("David J.");
        updateRequest.setEmail("new.mail@example.com");
        updateRequest.setClassType("Warrior");

        mockMvc.perform(put(PLAYERS_URL+"/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(playerId))
                .andExpect(jsonPath("$.name").value(updateRequest.getName()))
                .andExpect(jsonPath("$.email").value(updateRequest.getEmail()))
                .andExpect(jsonPath("$.class_type").value(updateRequest.getClassType()))
                .andExpect(jsonPath("$.level").value("1"))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundForUpdatingNonExistentPlayer() throws Exception {
        String nonExistentId = "non-existent-id";
        PlayerRequest updateRequest = new PlayerRequest();
        updateRequest.setName("No One");
        updateRequest.setEmail("no.mail@example.com");
        updateRequest.setClassType("Warrior");

        mockMvc.perform(put(PLAYERS_URL+"/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Player with ID " + nonExistentId + " not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeletePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        String responseContent = mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String playerId = objectMapper.readTree(responseContent).get("id").asText();

        mockMvc.perform(delete(PLAYERS_URL+"/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundForDeletingNonExistentPlayer() throws Exception {
        String nonExistentId = "non-existent-id";
        mockMvc.perform(delete(PLAYERS_URL+"/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Player not found with ID: " + nonExistentId));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnPlayerById() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("jones@example.com");
        request.setClassType("Mage");

        String responseContent = mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        
        String playerId = objectMapper.readTree(responseContent).get("id").asText();
        mockMvc.perform(get(PLAYERS_URL+"/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(playerId))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.class_type").value(request.getClassType()))
                .andExpect(jsonPath("$.level").value("1"))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundForGettingNonExistentPlayer() throws Exception {
        String nonExistentId = "non-existent-id";
        mockMvc.perform(get(PLAYERS_URL+"/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Player with ID " + nonExistentId + " not found"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldSearchPlayers() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Alice Wonderland");
        request.setEmail("alice@example.com");
        request.setClassType("Warrior");
        mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get(PLAYERS_URL)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,asc")
                .param("name", "Alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Alice Wonderland"))
                .andExpect(jsonPath("$.content[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$.content[0].class_type").value("Warrior"))
                .andExpect(jsonPath("$.content[0].level").value("1"))
                .andExpect(jsonPath("$.content[0].created_at").exists())
                .andExpect(jsonPath("$.content[0].updated_at").exists())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.total_elements").value(1))
                .andExpect(jsonPath("$.total_pages").value(1));
    }

    @Test
    void shouldReturnEmptyPageWhenNoPlayersMatchSearch() throws Exception {
        mockMvc.perform(get(PLAYERS_URL)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,asc")
                .param("name", "NonExistentName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.total_elements").value(0))
                .andExpect(jsonPath("$.total_pages").value(0));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessToCreatePlayerForNonAdminUser() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Unauthorized Create");
        request.setEmail("unauthorized@example.com");
        request.setClassType("Mage");

        mockMvc.perform(post(PLAYERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessToUpdatePlayerForNonAdminUser() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Unauthorized Update");
        request.setEmail("unauthorized@example.com");
        request.setClassType("Mage");

        mockMvc.perform(put(PLAYERS_URL+"/{id}", "some-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldDenyAccessToDeletePlayerForNonAdminUser() throws Exception {
        mockMvc.perform(delete(PLAYERS_URL+"/{id}", "some-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
