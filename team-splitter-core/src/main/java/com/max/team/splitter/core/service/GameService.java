package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.exception.NotFoundException;
import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import com.max.team.splitter.persistence.repositories.GameRepository;
import com.max.team.splitter.persistence.repositories.TeamEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.max.team.splitter.core.Constants.TEAM_COLORS;


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

    public List<Game> getGameByPoll(String pollId) {
        List<GameEntity> gameEntities = gameRepository.findByPollId(pollId);
        List<TeamEntryEntity> entities = teamEntryRepository.findByGameIdIn(gameEntities.stream().map(GameEntity::getId).collect(Collectors.toList()));
        Map<Long, List<TeamEntryEntity>> gameToEntries = entities.stream().collect(Collectors.groupingBy(TeamEntryEntity::getGameId));
        Map<Long, Player> playerMap = getPlayersMap(entities);

        List<Game> games = gameEntities.stream().map(CoreConverters::toGame).collect(Collectors.toList());
        for (Game game : games) {
            if (gameToEntries.containsKey(game.getId())) {
                Map<String, List<TeamEntryEntity>> byTeamName = gameToEntries.get(game.getId()).stream().collect(Collectors.groupingBy(TeamEntryEntity::getTeamName));
                Map<String, List<Player>> teamsMap = new LinkedHashMap<>();
                for (Map.Entry<String, List<TeamEntryEntity>> entry : byTeamName.entrySet()) {
                    teamsMap.put(entry.getKey(), entry.getValue().stream().map((item) -> playerMap.get(item.getPlayerId())).collect(Collectors.toList()));
                }

                List<Team> teams = new LinkedList<>();
                for (Map.Entry<String, List<Player>> entry : teamsMap.entrySet()) {
                    Team team = new Team();
                    team.setName(entry.getKey());
                    team.setPlayers(entry.getValue());
                    teams.add(team);
                }
                game.setTeams(teams);
            }
        }
        games.forEach((game -> {
            List<Team> teams = game.getTeams();
            Map<String, Integer> teamNameMap = new HashMap<>();
            for (int i = 0; i < TEAM_COLORS.length; i++) {
                teamNameMap.put(TEAM_COLORS[i], i);
            }
            //sort teams by color in the TEAMS_COLORS order
            teams.sort((a, b) -> {
                Integer aIndex = teamNameMap.getOrDefault(a.getName(), -1);
                Integer bIndex = teamNameMap.getOrDefault(b.getName(), -1);
                return aIndex.compareTo(bIndex);
            });
        }));

        return games;
    }

    private Map<Long, Player> getPlayersMap(List<TeamEntryEntity> teamEntryEntities) {
        Set<Long> ids = teamEntryEntities.stream().map(TeamEntryEntity::getPlayerId).collect(Collectors.toSet());
        List<Player> playersByIds = playerService.getPlayersByIds(ids);
        Map<Long, Player> playerMap = playersByIds.stream().collect(Collectors.toMap(Player::getId, Function.identity()));
        //set player score from team entry, hence current player score could be different
        teamEntryEntities.forEach((entry -> playerMap.get(entry.getPlayerId()).setScore(entry.getScore())));
        return playerMap;
    }

//    public void saveGameSplit(Map<String, List<Player>> teams, String pollId) {
//        GameEntity entity = new GameEntity();
//        entity.setCreationTimestamp(Instant.now());
//        entity.setPollId(pollId);
//        entity.setTeamSize(teams.size());
//
//        GameEntity savedGame = gameRepository.save(entity);
//        List<TeamEntryEntity> teamEntryEntities = new LinkedList<>();
//        for (Map.Entry<String, List<Player>> entry : teams.entrySet()) {
//            String teamColor = entry.getKey();
//            List<Player> players = entry.getValue();
//            for (Player player : players) {
//                TeamEntryEntity teamEntry = new TeamEntryEntity();
//                teamEntry.setTeamName(teamColor);
//                teamEntry.setPlayerId(player.getId());
//                teamEntry.setGameId(savedGame.getId());
//                teamEntry.setScore(player.getScore());
//                teamEntryEntities.add(teamEntry);
//            }
//        }
//
//        teamEntryRepository.saveAll(teamEntryEntities);
//    }

    public void saveGameScore(Long gameId, Integer blueScored, Integer redScored) {
        GameEntity gameEntity = gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException("Game id=" + gameId + " is not found"));
        gameEntity.setBlueScored(blueScored);
        gameEntity.setRedScored(redScored);
        gameRepository.save(gameEntity);


        if (blueScored.equals(redScored)) {
            return;
        }

        log.info("Updating players scores");
        List<TeamEntryEntity> winnerTeam;
        List<TeamEntryEntity> lostTeam;
        if (blueScored > redScored) {
            winnerTeam = teamEntryRepository.findByGameIdAndTeamName(gameId, "Blue");
            lostTeam = teamEntryRepository.findByGameIdAndTeamName(gameId, "Red");
        } else {
            winnerTeam = teamEntryRepository.findByGameIdAndTeamName(gameId, "Red");
            lostTeam = teamEntryRepository.findByGameIdAndTeamName(gameId, "Blue");
        }

        List<Player> winnerPlayers = playerService.getPlayersByIds(
                winnerTeam.stream().map(TeamEntryEntity::getPlayerId).collect(Collectors.toList()));
        for (Player winnerPlayer : winnerPlayers) {
            winnerPlayer.setGameScore(winnerPlayer.getGameScore() + 1);
            playerService.updatePlayer(winnerPlayer.getId(), winnerPlayer);
        };

        List<Player> lostPlayers = playerService.getPlayersByIds(
                lostTeam.stream().map(TeamEntryEntity::getPlayerId).collect(Collectors.toList()));

        for (Player lostPlayer : lostPlayers) {
            lostPlayer.setGameScore(lostPlayer.getGameScore() - 1);
            playerService.updatePlayer(lostPlayer.getId(), lostPlayer);
        };
        log.info("Player game_score update is completed");
    }

    @Transactional
    public void removeTeamEntry(Long gameId, Long playerId) {
        log.info("Deleting team entry by gameId={} and playerId={}", gameId, playerId);
        teamEntryRepository.deleteByGameIdAndPlayerId(gameId, playerId);
    }

    @Transactional
    public void deleteGame(Long gameId) {
        log.info("Deleting game by gameId={}", gameId);
        gameRepository.deleteById(gameId);
        teamEntryRepository.deleteByGameId(gameId);
    }

    @Transactional
    public void deleteGameAndTeamsForPoll(String pollId) {
        List<GameEntity> games = gameRepository.findByPollId(pollId);
        log.info("Found {} games for pollId={}", games.size(), pollId);

        for (GameEntity game : games) {
            log.info("Deleting team entry for gameId={}", game.getId());
            teamEntryRepository.deleteByGameId(game.getId());
        }

        gameRepository.deleteInBatch(games);
        log.info("Deleted {} games for pollId={}", games.size(), pollId);
    }

    public void deleteByGameSplitId(Long gameSplitId) {
        gameRepository.deleteByGameSplitId(gameSplitId);
    }
}
