package com.max.team.splitter.core.service;

import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import com.max.team.splitter.persistence.repositories.TeamEntryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.max.team.splitter.core.Constants.TEAM_COLORS;

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
        Map<Long, PlayerEntity> playerMap = getPlayersMap(teamEntryEntities);

        return toTeams(teamEntryEntities, playerMap);
    }

    private static List<Team> toTeams(List<TeamEntryEntity> teamEntryEntities, Map<Long, PlayerEntity> playerMap) {
        Map<String, List<TeamEntryEntity>> byTeamName = teamEntryEntities.stream().collect(Collectors.groupingBy(TeamEntryEntity::getTeamName));
        Map<String, List<PlayerEntity>> teamsMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<TeamEntryEntity>> entry : byTeamName.entrySet()) {
            teamsMap.put(entry.getKey(), entry.getValue().stream().map((item) -> playerMap.get(item.getPlayerId())).collect(Collectors.toList()));
        }

        List<Team> teams = new LinkedList<>();
        for (int i = 0; i < teamsMap.size(); i++) {
            String teamName = TEAM_COLORS[i];
            List<PlayerEntity> players = teamsMap.get(teamName);

            Team team = new Team();
            team.setName(teamName);
            team.setPlayers(players);
            teams.add(team);
        }

        return teams;
    }

    private Map<Long, PlayerEntity> getPlayersMap(List<TeamEntryEntity> teamEntryEntities) {
        Set<Long> ids = teamEntryEntities.stream().map(TeamEntryEntity::getPlayerId).collect(Collectors.toSet());
        List<PlayerEntity> playersByIds = playerService.getPlayersByIds(ids);
        Map<Long, PlayerEntity> playerMap = playersByIds.stream().collect(Collectors.toMap(PlayerEntity::getId, Function.identity()));
        //set player score from team entry, hence current player score could be different
        teamEntryEntities.forEach((entry -> playerMap.get(entry.getPlayerId()).setScore(entry.getScore())));
        return playerMap;
    }

    public List<Team> saveTeams(Map<String, List<PlayerEntity>> teamSplit, Long gameSplitId) {
        List<TeamEntryEntity> teamEntryEntities = new LinkedList<>();
        for (Map.Entry<String, List<PlayerEntity>> entry : teamSplit.entrySet()) {
            String teamColor = entry.getKey();
            List<PlayerEntity> players = entry.getValue();
            for (PlayerEntity player : players) {
                TeamEntryEntity teamEntry = new TeamEntryEntity();
                teamEntry.setTeamName(teamColor);
                teamEntry.setPlayerId(player.getId());
                teamEntry.setGameSplitId(gameSplitId);
                teamEntry.setScore(player.getScore());
                teamEntryEntities.add(teamEntry);
            }
        }

        List<TeamEntryEntity> saved = teamEntryRepository.saveAll(teamEntryEntities);

        Map<Long, PlayerEntity> playerMap = teamSplit.values().stream().flatMap(List::stream).collect(Collectors.toMap(PlayerEntity::getId, Function.identity()));
        return toTeams(saved, playerMap);
    }

    public void deleteByGameSplitId(Long gameSplitId, Long playerId) {
        teamEntryRepository.deleteByGameSplitIdAndPlayerId(gameSplitId, playerId);
    }

    public void deleteByGameSplitId(Long gameSplitId) {
        teamEntryRepository.deleteByGameSplitId(gameSplitId);
    }
}
