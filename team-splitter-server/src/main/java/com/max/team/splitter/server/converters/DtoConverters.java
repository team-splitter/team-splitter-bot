package com.max.team.splitter.server.converters;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.server.dto.PlayerDto;

public class DtoConverters {

    public static PlayerDto toPlayerDto(Player player) {
        PlayerDto dto = new PlayerDto();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setUsername(player.getUsername());
        dto.setScore(player.getScore());

        return dto;
    }

    public static Player toPlayer(PlayerDto dto) {
        Player player = new Player(dto.getId());
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setUsername(dto.getUsername());
        player.setScore(dto.getScore());

        return player;
    }
}
