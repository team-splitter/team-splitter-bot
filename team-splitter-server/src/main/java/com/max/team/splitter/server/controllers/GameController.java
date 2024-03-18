package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GameService gameService;


    @RequestMapping(method = RequestMethod.GET)
    public List<Game> getAllGames() {
        List<Game> games = gameService.getAllGames();
        games.sort(Comparator.comparing(Game::getCreationTime).reversed());
        return games;
    }


    @RequestMapping(value = "/{gameId}", method = RequestMethod.DELETE)
    public void deleteGame(@PathVariable("gameId") Long gameId) {
        gameService.deleteGame(gameId);
    }

}
