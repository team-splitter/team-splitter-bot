package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.strategy.TeamSplitStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.max.team.splitter.core.service.Constants.TEAM_COLORS;

@Service
public class HighestScoreTeamSplitterService implements TeamSplitterService {

    private static final int DEFAULT_SCORE = 30;
    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;

    private final TeamSplitStrategy splitStrategy;

    public HighestScoreTeamSplitterService(@Qualifier("BackAndForceTeamSplitStrategy") TeamSplitStrategy splitStrategy) {
        this.splitStrategy = splitStrategy;
    }

    @Override
    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players) {
        //Sort by score descending then by player id
        players.sort((a, b) -> {
            if (a.getScore() > b.getScore()) {
                return -1;
            } else if (a.getScore() < b.getScore()) {
                return 1;
            } else {
                return a.getId().compareTo(b.getId());
            }
        });

        List<List<Player>> teams = splitStrategy.split(numberOfTeams, players);
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

}
