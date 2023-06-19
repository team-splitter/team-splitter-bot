package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GameService gameService;


    @RequestMapping(value = "/poll/{pollId}", method = RequestMethod.GET)
    public List<Game> gamesByPollId(@PathVariable(name = "pollId") String pollId) {
        log.info("Getting list of games by pollId={}", pollId);
        List<Game> games = gameService.getGameByPoll(pollId);
        games.sort(Comparator.comparing(Game::getCreationTime).reversed());

        return games;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Game> getAllGames() {
        List<Game> games = gameService.getAllGames();
        games.sort(Comparator.comparing(Game::getCreationTime).reversed());
        return games;
    }
    @RequestMapping(value = "/{gameId}/score", method = RequestMethod.PUT)
    public void setGameScore(@PathVariable(name = "gameId") Long gameId,
                                @RequestParam(name = "blue") Integer blueScored,
                                @RequestParam(name = "red") Integer redScored) {
        gameService.saveGameScore(gameId, blueScored, redScored);

    }

    @RequestMapping(value = "/{gameId}/team_entry/{playerId}", method = RequestMethod.DELETE)
    public void deleteTeamEntry(@PathVariable("gameId") Long gameId,
                                @PathVariable("playerId") Long playerId) {
        gameService.removeTeamEntry(gameId, playerId);
    }

    @RequestMapping(value = "/{gameId}", method = RequestMethod.DELETE)
    public void deleteGame(@PathVariable("gameId") Long gameId) {
        gameService.deleteGame(gameId);
    }

}
