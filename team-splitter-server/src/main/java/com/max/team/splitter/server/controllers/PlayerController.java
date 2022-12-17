package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.ScoresService;
import com.max.team.splitter.server.converters.DtoConverters;
import com.max.team.splitter.server.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ScoresService scoresService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PlayerDto> getPlayers() {
        List<Player> players = playerService.getPlayers();
        Map<Long, Integer> playerScores = scoresService.getScores();
        List<PlayerDto> dtos = new LinkedList<>();
        for (Player player : players) {
            PlayerDto dto = DtoConverters.toPlayerDto(player);
            dto.setScore(playerScores.get(player.getId()));
            dtos.add(dto);
        }
        return dtos;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlayerDto getPlayerById(@PathVariable(name = "id") Long id) {
        Player player = playerService.getPlayer(id);
        Map<Long, Integer> playerScores = scoresService.getScores();


        PlayerDto dto = DtoConverters.toPlayerDto(player);
        dto.setScore(playerScores.get(id));
        return dto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public PlayerDto savePlayer(@RequestBody PlayerDto dto) {
        playerService.addPlayer(DtoConverters.toPlayer(dto));
        Long playerId = dto.getId();
        scoresService.saveScore(playerId, dto.getScore());
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PlayerDto updatePlayer(@PathVariable("id") Long id, @RequestBody PlayerDto dto) {
        playerService.updatePlayer(id, DtoConverters.toPlayer(dto));
        Long playerId = dto.getId();
        scoresService.saveScore(playerId, dto.getScore());
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
    }

}
