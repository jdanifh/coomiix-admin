package com.coomiix.admin.player.infrastructure.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.coomiix.admin.model.PlayerResponse;
import com.coomiix.admin.player.domain.Player;

@Mapper
public interface PlayerResponseMapper {
    PlayerResponseMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(PlayerResponseMapper.class);

    @Mapping(target = "email", source = "email.value")
    PlayerResponse toPlayerResponse(Player player);

}
