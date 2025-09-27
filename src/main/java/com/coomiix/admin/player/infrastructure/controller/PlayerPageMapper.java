package com.coomiix.admin.player.infrastructure.controller;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.coomiix.admin.model.PlayerPage;
import com.coomiix.admin.model.PlayerResponse;
import com.coomiix.admin.player.domain.Player;

@Mapper
public abstract class PlayerPageMapper {
    public static final PlayerPageMapper INSTANCE = Mappers.getMapper(PlayerPageMapper.class);

    @Mapping(target = "content", expression = "java(mapContent(playerPage))")
    @Mapping(target = "page", source = "playerPage.pageable.pageNumber")
    @Mapping(target = "size", source = "playerPage.pageable.pageSize")
    public abstract PlayerPage toPlayerPage(Page<Player> playerPage);

    protected static List<PlayerResponse> mapContent(Page<Player> players) {
        return players.map(PlayerResponseMapper.INSTANCE::toPlayerResponse).toList();
    }
}
