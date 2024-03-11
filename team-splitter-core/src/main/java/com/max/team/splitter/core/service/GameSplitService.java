package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.model.GameSplit;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.GameSplitEntity;
import com.max.team.splitter.persistence.repositories.GameRepository;
import com.max.team.splitter.persistence.repositories.GameSplitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.max.team.splitter.core.Constants.DEFAULT_SCORE;

@Service
public class GameSplitService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final GameSplitRepository gameSplitRepository;
    private final TeamService teamService;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final PollService pollService;
    private final PlayerService playerService;
    private final TeamSplitterService teamSplitterService;

    public GameSplitService(GameSplitRepository gameSplitRepository, TeamService teamService, GameRepository gameRepository,
                            GameService gameService, PollService pollService,
                            PlayerService playerService, TeamSplitterService teamSplitterService) {
        this.gameSplitRepository = gameSplitRepository;
        this.teamService = teamService;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.pollService = pollService;
        this.playerService = playerService;
        this.teamSplitterService = teamSplitterService;
    }


    @Transactional
    public GameSplit split(String pollId, int numberOfTeams, SplitterStrategyType splitType) {
        List<Long> playerIds = pollService.getPlayerIdsGoingToGame(pollId);
        List<Player> players = playerService.getPlayersByIds(playerIds);
        players.forEach(player -> player.setScore(player.getScore() != null ? player.getScore() : DEFAULT_SCORE));


        log.info("Split players={}", players);
        Map<String, List<Player>> teams = teamSplitterService.splitTeams(numberOfTeams, splitType, players);
        GameSplit gameSplit = saveGameSplit(teams, pollId, numberOfTeams, splitType);

        return gameSplit;
    }

    public List<GameSplit> getGameSplitsByPollId(String pollId) {
        List<GameSplitEntity> entities = gameSplitRepository.findByPollId(pollId);

        List<GameSplit> splits = new ArrayList<>();
        for (GameSplitEntity entity : entities) {
            List<Team> teams = teamService.getTeamsForGameSplit(entity.getId());
            List<Game> games = gameService.getGamesByGameSplit(entity.getId());

            splits.add(CoreConverters.toGameSplit(entity, games, teams));
        }

        return splits;
    }

    public List<GameSplit> getAllGameSplits() {
        List<GameSplitEntity> entities = gameSplitRepository.findAll();
        Map<Long, List<Game>> gamesMap = gameService.getAllGames().stream().collect(Collectors.groupingBy(Game::getGameSplitId));


        List<GameSplit> splits = new ArrayList<>();
        for (GameSplitEntity entity : entities) {
            splits.add(CoreConverters.toGameSplit(entity, gamesMap.getOrDefault(entity.getId(), Collections.emptyList()), Collections.emptyList()));
        }

        return splits;
    }

    private GameSplit saveGameSplit(Map<String, List<Player>> teams, String pollId, int numberOfTeams, SplitterStrategyType splitType) {
        GameSplitEntity gameSplitEntity = new GameSplitEntity();
        gameSplitEntity.setPollId(pollId);
        gameSplitEntity.setTeamSize(numberOfTeams);
        gameSplitEntity.setSplitAlg(splitType.name());
        gameSplitEntity.setCreationTimestamp(Instant.now());
        GameSplitEntity savedGameSplitEntity = gameSplitRepository.save(gameSplitEntity);

        //TODO: remove after migration
        GameEntity gameEntity = new GameEntity();
        gameEntity.setCreationTimestamp(Instant.now());
        gameEntity.setPollId(pollId);
        gameEntity.setTeamSize(teams.size());
        gameEntity.setGameSplitId(savedGameSplitEntity.getId());
        GameEntity savedGame = gameRepository.save(gameEntity);


        List<Team> saveTeams = teamService.saveTeams(teams, savedGame.getId(), gameSplitEntity.getId());


        return CoreConverters.toGameSplit(savedGameSplitEntity, Collections.emptyList(), saveTeams);
    }

    public void deleteTeamEntry(Long gameSplitId, Long playerId) {
        teamService.deleteByGameSplitId(gameSplitId, playerId);
    }

    @Transactional
    public void deleteGameSplit(Long gameSplitId) {
        gameService.deleteByGameSplitId(gameSplitId);
        teamService.deleteByGameSplitId(gameSplitId);
        gameSplitRepository.deleteById(gameSplitId);
    }
}
