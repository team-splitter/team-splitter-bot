package com.max.team.splitter.server.converters;

import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.server.dto.GameDto;
import com.max.team.splitter.server.dto.PlayerDto;
import com.max.team.splitter.server.dto.TeamDto;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<GameDto> toGameDtos(List<Game> games) {
        return games.stream().map(DtoConverters::toGameDto).collect(Collectors.toList());
    }
    public static GameDto toGameDto(Game game) {
        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setPollId(game.getPollId());
        dto.setCreationTime(game.getCreationTime());
        dto.setBlueScored(game.getBlueScored());
        dto.setRedScored(game.getRedScored());
        dto.setTeams(game.getTeams().entrySet().stream()
                .map((entry -> {
                    TeamDto teamDto = new TeamDto();
                    teamDto.setName(entry.getKey());
                    teamDto.setPlayers(entry.getValue().stream()
                            .map(DtoConverters::toPlayerDto)
                            .collect(Collectors.toList()));
                    return teamDto;
                }))
                .collect(Collectors.toList()));
        return dto;
    }
}
