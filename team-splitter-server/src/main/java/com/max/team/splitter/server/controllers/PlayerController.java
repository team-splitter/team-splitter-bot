package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.ScoresService;
import com.max.team.splitter.server.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
            PlayerDto dto = new PlayerDto();
            dto.setId(player.getId());
            dto.setFirstName(player.getFirstName());
            dto.setLastName(player.getLastName());
            dto.setUsername(player.getUsername());

            dto.setScore(playerScores.get(player.getId()));
            dtos.add(dto);
        }
        return dtos;
    }


}
