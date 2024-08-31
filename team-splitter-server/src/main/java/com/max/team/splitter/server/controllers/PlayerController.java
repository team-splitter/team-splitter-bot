package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> players = playerService.getPlayers();
        return players;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlayerEntity getPlayerById(@PathVariable(name = "id") Long id) {
        return playerService.getPlayer(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PlayerEntity createPlayer(@RequestBody PlayerEntity dto) {
        playerService.createPlayer(dto);
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PlayerEntity updatePlayer(@PathVariable("id") Long id, @RequestBody PlayerEntity dto) {
        playerService.updatePlayer(id, dto);
        return dto;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
    }

}
