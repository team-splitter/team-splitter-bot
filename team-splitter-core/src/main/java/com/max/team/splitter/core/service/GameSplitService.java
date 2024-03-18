package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.model.*;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.GameSplitEntity;
import com.max.team.splitter.persistence.repositories.GameRepository;
import com.max.team.splitter.persistence.repositories.GameSplitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
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

    public Page<GameSplit> getAllGameSplits(Pageable pageable) {
        Page<GameSplitEntity> page = gameSplitRepository.findAll(pageable);
        Map<Long, List<Game>> gamesMap = gameService.getAllGames().stream().collect(Collectors.groupingBy(Game::getGameSplitId));

        List<GameSplit> splits = new ArrayList<>();
        for (GameSplitEntity entity : page) {
            splits.add(CoreConverters.toGameSplit(entity, gamesMap.getOrDefault(entity.getId(), Collections.emptyList()), Collections.emptyList()));
        }

        return CoreConverters.toPage(splits, page);
    }

    private GameSplit saveGameSplit(Map<String, List<Player>> teams, String pollId, int numberOfTeams, SplitterStrategyType splitType) {
        GameSplitEntity gameSplitEntity = new GameSplitEntity();
        gameSplitEntity.setPollId(pollId);
        gameSplitEntity.setTeamSize(numberOfTeams);
        gameSplitEntity.setSplitAlg(splitType.name());
        gameSplitEntity.setCreationTime(Instant.now());
        GameSplitEntity savedGameSplitEntity = gameSplitRepository.save(gameSplitEntity);


        List<Team> saveTeams = teamService.saveTeams(teams, gameSplitEntity.getId());


        return CoreConverters.toGameSplit(savedGameSplitEntity, Collections.emptyList(), saveTeams);
    }

    @Transactional
    public void deleteTeamEntry(Long gameSplitId, Long playerId) {
        teamService.deleteByGameSplitId(gameSplitId, playerId);
    }

    @Transactional
    public void deleteGameSplit(Long gameSplitId) {
        gameService.deleteByGameSplitId(gameSplitId);
        teamService.deleteByGameSplitId(gameSplitId);
        gameSplitRepository.deleteById(gameSplitId);
    }

    @Transactional
    public GameSplit setScores(Long gameSplitId, List<GameScore> scores) {
        gameRepository.deleteByGameSplitId(gameSplitId);

        GameSplitEntity gameSplit = gameSplitRepository.getOne(gameSplitId);
        Map<String, Team> teamNameToTeam = teamService.getTeamsForGameSplit(gameSplitId).stream().collect(Collectors.toMap(Team::getName, Function.identity()));
        List<GameEntity> games = scores.stream().map((gameScore -> {
            GameEntity game = new GameEntity();
            game.setGameSplitId(gameSplitId);
            game.setCreationTimestamp(Instant.now());
            game.setTeamOneName(gameScore.getTeamOneName());
            game.setTeamTwoName(gameScore.getTeamTwoName());
            game.setTeamOneScored(gameScore.getTeamOneScored());
            game.setTeamTwoScored(gameScore.getTeamTwoScored());
            return game;
        })).collect(Collectors.toList());

        List<GameEntity> savedGames = gameRepository.saveAll(games);


        //update player scores
        Map<String, Integer> teamPoints = calculateTeamPoints(scores);
        log.info("Team points={} for gameSplitId={}", teamPoints, gameSplitId);

        Map<Long, Integer> playerPoints = calculatePlayerPoints(teamPoints, teamNameToTeam);
        playerService.updatePlayerScores(playerPoints);

        return CoreConverters.toGameSplit(gameSplit, savedGames.stream().map(CoreConverters::toGame).collect(Collectors.toList()), Collections.emptyList());
    }

    private Map<Long, Integer> calculatePlayerPoints(Map<String, Integer> teamPoints, Map<String, Team> teamNameToTeam) {
        Map<Long, Integer> playerPoints = new LinkedHashMap<>();

        for (Map.Entry<String, Team> entry : teamNameToTeam.entrySet()) {
            String teamName = entry.getKey();
            List<Player> players = entry.getValue().getPlayers();

            Integer points = teamPoints.getOrDefault(teamName, 0);
            if (points == 0) continue;

            for (Player player : players) {
                playerPoints.put(player.getId(), points);
            }
        }
        return playerPoints;
    }

    private Map<String, Integer> calculateTeamPoints(List<GameScore> scores) {
        Map<String, Integer> points = new LinkedHashMap<>();
        for (GameScore score : scores) {
            points.putIfAbsent(score.getTeamOneName(), 0);
            points.putIfAbsent(score.getTeamTwoName(), 0);

            if (score.getTeamOneScored() > score.getTeamTwoScored()) {
                points.put(score.getTeamOneName(), points.get(score.getTeamOneName()) + 1);
                points.put(score.getTeamTwoName(), points.get(score.getTeamTwoName()) - 1);
            } else if (score.getTeamOneScored() < score.getTeamTwoScored()) {
                points.put(score.getTeamOneName(), points.get(score.getTeamOneName()) - 1);
                points.put(score.getTeamTwoName(), points.get(score.getTeamTwoName()) + 1);
            }
        }

        return points;
    }
}
