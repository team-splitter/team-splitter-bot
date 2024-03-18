package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import com.max.team.splitter.persistence.repositories.TeamEntryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamEntryRepository teamEntryRepository;

    private final PlayerService playerService;

    public TeamService(TeamEntryRepository teamEntryRepository, PlayerService playerService) {
        this.teamEntryRepository = teamEntryRepository;
        this.playerService = playerService;
    }

    public List<Team> getTeamsForGameSplit(Long gameSplitId) {
        List<TeamEntryEntity> teamEntryEntities = teamEntryRepository.findByGameSplitId(gameSplitId);
        Map<Long, Player> playerMap = getPlayersMap(teamEntryEntities);

        return toTeams(teamEntryEntities, playerMap);
    }

    private static List<Team> toTeams(List<TeamEntryEntity> teamEntryEntities, Map<Long, Player> playerMap) {
        Map<String, List<TeamEntryEntity>> byTeamName = teamEntryEntities.stream().collect(Collectors.groupingBy(TeamEntryEntity::getTeamName));
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

        return teams;
    }

    private Map<Long, Player> getPlayersMap(List<TeamEntryEntity> teamEntryEntities) {
        Set<Long> ids = teamEntryEntities.stream().map(TeamEntryEntity::getPlayerId).collect(Collectors.toSet());
        List<Player> playersByIds = playerService.getPlayersByIds(ids);
        Map<Long, Player> playerMap = playersByIds.stream().collect(Collectors.toMap(Player::getId, Function.identity()));
        //set player score from team entry, hence current player score could be different
        teamEntryEntities.forEach((entry -> playerMap.get(entry.getPlayerId()).setScore(entry.getScore())));
        return playerMap;
    }

    public List<Team> saveTeams(Map<String, List<Player>> teamSplit, Long gameSplitId) {
        List<TeamEntryEntity> teamEntryEntities = new LinkedList<>();
        for (Map.Entry<String, List<Player>> entry : teamSplit.entrySet()) {
            String teamColor = entry.getKey();
            List<Player> players = entry.getValue();
            for (Player player : players) {
                TeamEntryEntity teamEntry = new TeamEntryEntity();
                teamEntry.setTeamName(teamColor);
                teamEntry.setPlayerId(player.getId());
                teamEntry.setGameSplitId(gameSplitId);
                teamEntry.setScore(player.getScore());
                teamEntryEntities.add(teamEntry);
            }
        }

        List<TeamEntryEntity> saved = teamEntryRepository.saveAll(teamEntryEntities);

        Map<Long, Player> playerMap = teamSplit.values().stream().flatMap(List::stream).collect(Collectors.toMap(Player::getId, Function.identity()));
        return toTeams(saved, playerMap);
    }

    public void deleteByGameSplitId(Long gameSplitId, Long playerId) {
        teamEntryRepository.deleteByGameSplitIdAndPlayerId(gameSplitId, playerId);
    }

    public void deleteByGameSplitId(Long gameSplitId) {
        teamEntryRepository.deleteByGameSplitId(gameSplitId);
    }
}
