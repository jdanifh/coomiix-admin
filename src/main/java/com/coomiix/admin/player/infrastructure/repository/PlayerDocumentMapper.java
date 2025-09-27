package com.coomiix.admin.player.infrastructure.repository;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.coomiix.admin.player.domain.Player;

@Mapper
public interface PlayerDocumentMapper {
    PlayerDocumentMapper INSTANCE = Mappers.getMapper(PlayerDocumentMapper.class);

    Player toPlayer(PlayerDocument document);
    PlayerDocument toPlayerDocument(Player player);

}
