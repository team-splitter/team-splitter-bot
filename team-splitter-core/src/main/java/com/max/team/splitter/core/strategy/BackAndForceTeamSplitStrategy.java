package com.max.team.splitter.core.strategy;

import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "BackAndForceTeamSplitStrategy")
public class BackAndForceTeamSplitStrategy implements TeamSplitStrategy{

    public List<List<PlayerEntity>> split(int numberOfTeams, List<PlayerEntity> players) {
        List<List<PlayerEntity>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int i = 0;
        int direction = -1;
        for (PlayerEntity player : players) {
            if (i % numberOfTeams == 0) {
                direction = direction == -1 ? 0 : -1;
            }
            int teamIndex = direction == 0 ? i % numberOfTeams : numberOfTeams - 1 - (i%numberOfTeams);
            teams.get(teamIndex).add(player);
            i++;
        }
        return teams;
    }

    @Override
    public SplitterStrategyType getType() {
        return SplitterStrategyType.BACK_AND_FORCE;
    }
}
