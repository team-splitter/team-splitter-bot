package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.PlayerStat;
import com.max.team.splitter.core.service.PlayerStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/player_stat")
public class PlayerStatController {
    private static final Logger log = LoggerFactory.getLogger(PlayerStatController.class);
    @Autowired
    private PlayerStatService playerStatService;

    @RequestMapping(method = RequestMethod.GET)
    public List<PlayerStat> getPlayerStats() {
        log.info("Getting player stats");
        return playerStatService.getPlayerStats();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PlayerStat getPlayerById(@PathVariable(name = "id") Long playerId) {
        log.info("Getting player stat for player id: {}", playerId);
        return playerStatService.getPlayerStat(playerId);
    }


}
