package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PlayerScore;
import com.max.team.splitter.core.strategy.TeamSplitStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.max.team.splitter.core.service.Constants.TEAM_COLORS;

@Service
public class HighestScoreTeamSplitterService implements TeamSplitterService {

    private static final int DEFAULT_SCORE = 30;
    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;

    private final ScoresService scoresService;
    private final TeamSplitStrategy splitStrategy;

    public HighestScoreTeamSplitterService(ScoresService scoresService,
                                           @Qualifier("BackAndForceTeamSplitStrategy") TeamSplitStrategy splitStrategy) {
        this.scoresService = scoresService;
        this.splitStrategy = splitStrategy;
    }

    @Override
    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players) {
        List<PlayerScore> playerScores = getPlayerScores(players);

        //Sort by score descending then by player id
        playerScores.sort((a, b) -> {
            if (a.getScore() > b.getScore()) {
                return -1;
            } else if (a.getScore() < b.getScore()) {
                return 1;
            } else {
                return a.getPlayer().getId().compareTo(b.getPlayer().getId());
            }
        });

        List<List<Player>> teams = splitStrategy.split(numberOfTeams, playerScores);
        return teams;
    }

    public Map<String, List<Player>> splitTeams(int numberOfTeams, List<Player> players) {
        List<List<Player>> teams = splitIntoTeams(numberOfTeams, players);
        LinkedHashMap<String, List<Player>> teamMap = new LinkedHashMap<>();
        int teamNum = 0;
        for (List<Player> team : teams) {
            String teamColor = TEAM_COLORS[teamNum];

            teamMap.put(teamColor, team);
            teamNum++;
        }

        return teamMap;
    }

    private List<PlayerScore> getPlayerScores(List<Player> players) {
        Map<Long, Integer> scores = scoresService.getScores();
        return players.stream()
                .map((player -> new PlayerScore(player, scores.getOrDefault(player.getId(), DEFAULT_SCORE))))
                .collect(Collectors.toList());
    }
}
