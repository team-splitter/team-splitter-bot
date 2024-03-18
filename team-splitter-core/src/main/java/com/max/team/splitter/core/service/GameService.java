package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.repositories.GameRepository;
import com.max.team.splitter.persistence.repositories.TeamEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GameService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GameRepository gameRepository;
    private final TeamEntryRepository teamEntryRepository;
    private final PlayerService playerService;

    public GameService(GameRepository gameRepository, TeamEntryRepository teamEntryRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.teamEntryRepository = teamEntryRepository;
        this.playerService = playerService;
    }

    public List<Game> getAllGames() {
        List<GameEntity> gameEntities = gameRepository.findAll();
        List<Game> games = gameEntities.stream().map(CoreConverters::toGame)
                .sorted(Comparator.comparing(Game::getCreationTime).reversed())
                .collect(Collectors.toList());

        return games;
    }

    public List<Game> getGamesByGameSplit(Long gameSplitId) {
        List<GameEntity> entities = gameRepository.findByGameSplitId(gameSplitId);

        return entities.stream().map(CoreConverters::toGame).collect(Collectors.toList());
    }

    @Transactional
    public void deleteGame(Long gameId) {
        log.info("Deleting game by gameId={}", gameId);
        gameRepository.deleteById(gameId);
    }

        public void deleteByGameSplitId(Long gameSplitId) {
        gameRepository.deleteByGameSplitId(gameSplitId);
    }
}
