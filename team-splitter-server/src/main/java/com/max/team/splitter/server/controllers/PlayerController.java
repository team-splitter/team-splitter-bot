package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.server.converters.DtoConverters;
import com.max.team.splitter.server.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PlayerDto> getPlayers() {
        List<Player> players = playerService.getPlayers();
        List<PlayerDto> dtos = new LinkedList<>();
        for (Player player : players) {
            PlayerDto dto = DtoConverters.toPlayerDto(player);
            dtos.add(dto);
        }
        return dtos;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlayerDto getPlayerById(@PathVariable(name = "id") Long id) {
        Player player = playerService.getPlayer(id);

        PlayerDto dto = DtoConverters.toPlayerDto(player);
        return dto;
    }

    @RequestMapping(method = RequestMethod.POST)
    public PlayerDto createPlayer(@RequestBody PlayerDto dto) {
        playerService.createPlayer(DtoConverters.toPlayer(dto));
        Long playerId = dto.getId();
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PlayerDto updatePlayer(@PathVariable("id") Long id, @RequestBody PlayerDto dto) {
        playerService.updatePlayer(id, DtoConverters.toPlayer(dto));
        Long playerId = dto.getId();
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
    }

}
