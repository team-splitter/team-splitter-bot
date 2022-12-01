package com.max.team.splitter.service;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import com.max.team.splitter.persistence.repositories.GameRepository;
import com.max.team.splitter.persistence.repositories.TeamEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class GameService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GameRepository gameRepository;
    private final TeamEntryRepository teamEntryRepository;

    public GameService(GameRepository gameRepository, TeamEntryRepository teamEntryRepository) {
        this.gameRepository = gameRepository;
        this.teamEntryRepository = teamEntryRepository;
    }

    public void saveGameSplit(Map<String, List<Player>> teams, String pollId) {
        GameEntity entity = new GameEntity();
        entity.setCreationTimestamp(Instant.now());
        entity.setPollId(pollId);

        GameEntity savedGame = gameRepository.save(entity);
        List<TeamEntryEntity> teamEntryEntities = new LinkedList<>();
        for (Map.Entry<String, List<Player>> entry : teams.entrySet()) {
            String teamColor = entry.getKey();
            List<Player> players = entry.getValue();
            for (Player player : players) {
                TeamEntryEntity teamEntry = new TeamEntryEntity();
                teamEntry.setTeamName(teamColor);
                teamEntry.setPlayerId(player.getId());
                teamEntry.setGameId(savedGame.getId());
                teamEntryEntities.add(teamEntry);
            }
        }

        teamEntryRepository.saveAll(teamEntryEntities);
    }
}
