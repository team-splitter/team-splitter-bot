package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.service.GameService;
import com.max.team.splitter.server.converters.DtoConverters;
import com.max.team.splitter.server.dto.GameDto;
import com.max.team.splitter.server.dto.TeamDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game")
public class GameController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GameService gameService;


    @RequestMapping(value = "/poll/{pollId}", method = RequestMethod.GET)
    public List<GameDto> gamesByPollId(@PathVariable(name = "pollId") String pollId) {
        log.info("Getting list of games by pollId={}", pollId);
        List<Game> games = gameService.getGameByPoll(pollId);
        games.sort(Comparator.comparing(Game::getCreationTime).reversed());

        List<GameDto> gameDtos = games.stream().map((game -> {
            GameDto dto = new GameDto();
            dto.setId(game.getId());
            dto.setPollId(game.getPollId());
            dto.setCreationTime(game.getCreationTime());
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
        })).collect(Collectors.toList());

        return gameDtos;
    }

}
