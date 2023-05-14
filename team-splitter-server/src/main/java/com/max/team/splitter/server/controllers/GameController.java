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
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.max.team.splitter.server.converters.DtoConverters.toGameDtos;

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

        return toGameDtos(games);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GameDto> allGames() {
        return toGameDtos(gameService.getAllGames());
    }
    @RequestMapping(value = "/{gameId}/score", method = RequestMethod.PUT)
    public void setGameScore(@PathVariable(name = "gameId") Long gameId,
                                @RequestParam(name = "blue") Integer blueScored,
                                @RequestParam(name = "red") Integer redScored) {
        gameService.saveGameScore(gameId, blueScored, redScored);

    }

    @RequestMapping(value = "/{gameId}/team_entry/{teamEntryId}", method = RequestMethod.DELETE)
    public void deleteTeamEntry(@PathVariable("gameId") Long gameId,
                                @PathVariable("teamEntryId") Long teamEntryId) {
        gameService.removeTeamEntry(gameId, teamEntryId);
    }

}
