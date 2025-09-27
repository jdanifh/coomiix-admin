package com.coomiix.admin.player.infrastructure.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.coomiix.admin.model.PlayerResponse;
import com.coomiix.admin.player.domain.Player;

@Mapper
public interface PlayerResponseMapper {
    PlayerResponseMapper INSTANCE = Mappers.getMapper(PlayerResponseMapper.class);

    @Mapping(target = "email", source = "email.value")
    PlayerResponse toPlayerResponse(Player player);

}
