package com.max.team.splitter.core.service;

import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.core.strategy.TeamSplitStrategy;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.max.team.splitter.core.Constants.TEAM_COLORS;


@Service
public class HighestScoreTeamSplitterService implements TeamSplitterService {

    private final Map<SplitterStrategyType, TeamSplitStrategy> strategyMap;

    public HighestScoreTeamSplitterService(List<TeamSplitStrategy> splitStrategies) {
        strategyMap = new HashMap<>();
        for (TeamSplitStrategy splitStrategy : splitStrategies) {
            strategyMap.put(splitStrategy.getType(), splitStrategy);
        }
    }

    @Override
    public List<List<PlayerEntity>> splitIntoTeams(int numberOfTeams, SplitterStrategyType splitType,  List<PlayerEntity> players) {

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

        List<List<PlayerEntity>> teams = strategyMap.get(splitType).split(numberOfTeams, players);
        return teams;
    }

    public Map<String, List<PlayerEntity>> splitTeams(int numberOfTeams,  SplitterStrategyType splitType, List<PlayerEntity> players) {
        List<List<PlayerEntity>> teams = splitIntoTeams(numberOfTeams, splitType, players);
        LinkedHashMap<String, List<PlayerEntity>> teamMap = new LinkedHashMap<>();
        int teamNum = 0;
        for (List<PlayerEntity> team : teams) {
            String teamColor = TEAM_COLORS[teamNum];

            teamMap.put(teamColor, team);
            teamNum++;
        }

        return teamMap;
    }

}
