package com.coomiix.admin.player.application.update;

import com.coomiix.admin.model.PlayerRequest;

public record UpdatePlayerCommand(String id, String name, String email, String classType) {

    public static UpdatePlayerCommand of(String id, PlayerRequest playerRequest) {
        return new UpdatePlayerCommand(id, playerRequest.getName(), playerRequest.getEmail(),
                playerRequest.getClassType());
    }
}
