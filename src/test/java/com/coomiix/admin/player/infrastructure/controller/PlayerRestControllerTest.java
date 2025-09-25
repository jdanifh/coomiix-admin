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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerRestControllerTest extends TestContainerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldCreatePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        mockMvc.perform(post("/api/v1/players").with(csrf())
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
    @WithMockUser
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Invalid Email");
        request.setEmail("not-an-email");
        request.setClassType("Mage");

        mockMvc.perform(post("/api/v1/players").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALUE_NOT_VALID"))
                .andExpect(jsonPath("$.message").value("Invalid email address"));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestForEmptyEmail() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("Empty Email");
        request.setEmail("");
        request.setClassType("Mage");

        mockMvc.perform(post("/api/v1/players").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALUE_NOT_VALID"))
                .andExpect(jsonPath("$.message").value("Email cannot be null or empty"));
    }

    @Test
    @WithMockUser
    void shouldUpdatePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        String responseContent = mockMvc.perform(post("/api/v1/players").with(csrf())
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

        mockMvc.perform(put("/api/v1/players/{id}", playerId).with(csrf())
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
    @WithMockUser
    void shouldReturnNotFoundForUpdatingNonExistentPlayer() throws Exception {
        String nonExistentId = "non-existent-id";
        PlayerRequest updateRequest = new PlayerRequest();
        updateRequest.setName("No One");
        updateRequest.setEmail("no.mail@example.com");
        updateRequest.setClassType("Warrior");

        mockMvc.perform(put("/api/v1/players/{id}", nonExistentId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Player with ID " + nonExistentId + " not found"));
    }

    @Test
    @WithMockUser
    void shouldDeletePlayerEndpoint() throws Exception {
        PlayerRequest request = new PlayerRequest();
        request.setName("David Jones");
        request.setEmail("david.jones@example.com");
        request.setClassType("Mage");

        String responseContent = mockMvc.perform(post("/api/v1/players").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        String playerId = objectMapper.readTree(responseContent).get("id").asText();

        mockMvc.perform(delete("/api/v1/players/{id}", playerId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundForDeletingNonExistentPlayer() throws Exception {
        String nonExistentId = "non-existent-id";
        mockMvc.perform(delete("/api/v1/players/{id}", nonExistentId).with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Player not found with ID: " + nonExistentId));
    }
}
