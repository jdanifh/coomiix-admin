package com.coomiix.admin.player.infrastructure.repository;

import org.mapstruct.Mapper;

import com.coomiix.admin.player.domain.Player;

@Mapper
public interface PlayerDocumentMapper {
    PlayerDocumentMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(PlayerDocumentMapper.class);

    Player toPlayer(PlayerDocument document);
    PlayerDocument toPlayerDocument(Player player);

}
