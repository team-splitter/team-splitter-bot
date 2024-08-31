package com.max.team.splitter.core.strategy;

import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "CircularTeamSplitStrategy")
public class CircularTeamSplitStrategy implements TeamSplitStrategy{
    @Override
    public List<List<PlayerEntity>> split(int numberOfTeams, List<PlayerEntity> players) {
        List<List<PlayerEntity>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int i = 0;
        for (PlayerEntity player : players) {
            teams.get(i % numberOfTeams).add(player);
            i++;
        }
        return teams;
    }

    @Override
    public SplitterStrategyType getType() {
        return SplitterStrategyType.CIRCULAR;
    }

}
