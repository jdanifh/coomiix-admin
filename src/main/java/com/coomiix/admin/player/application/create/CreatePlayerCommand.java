package com.coomiix.admin.player.application.create;

import com.coomiix.admin.model.PlayerRequest;

public record CreatePlayerCommand(String name, String email, String classType) {

    public static CreatePlayerCommand of(PlayerRequest playerRequest) {
        return new CreatePlayerCommand(playerRequest.getName(), playerRequest.getEmail(),
                playerRequest.getClassType());
    }

}
