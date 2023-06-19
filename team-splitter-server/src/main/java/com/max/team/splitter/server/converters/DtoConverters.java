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
        dto.setGameScore(player.getGameScore());
        dto.setPrivacy(player.getPrivacy());

        return dto;
    }

    public static Player toPlayer(PlayerDto dto) {
        Player player = new Player(dto.getId());
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());
        player.setUsername(dto.getUsername());
        player.setScore(dto.getScore());
        player.setGameScore(dto.getGameScore());
        player.setPrivacy(dto.getPrivacy());

        return player;
    }
}
