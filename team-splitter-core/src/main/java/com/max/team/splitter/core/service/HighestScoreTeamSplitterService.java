package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.strategy.TeamSplitStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.max.team.splitter.core.service.Constants.TEAM_COLORS;

@Service
public class HighestScoreTeamSplitterService implements TeamSplitterService {

    private final TeamSplitStrategy splitStrategy;

    public HighestScoreTeamSplitterService(@Qualifier("BackAndForceTeamSplitStrategy") TeamSplitStrategy splitStrategy) {
        this.splitStrategy = splitStrategy;
    }

    @Override
    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players) {
        //Sort by score descending then by player id
        int DIVIDER = 5;
        long dayOfTheMonth = LocalDate.now().get(ChronoField.DAY_OF_MONTH);
        players.sort((a, b) -> {
            if (a.getScore() > b.getScore()) {
                return -1;
            } else if (a.getScore() < b.getScore()) {
                return 1;
            } else {
                //Shifts same scores players depends on the day of the month
                Long id1 = (a.getId() % DIVIDER + dayOfTheMonth) % DIVIDER ;
                Long id2 = (b.getId() % DIVIDER + dayOfTheMonth) % DIVIDER;
                return id1.compareTo(id2);
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
