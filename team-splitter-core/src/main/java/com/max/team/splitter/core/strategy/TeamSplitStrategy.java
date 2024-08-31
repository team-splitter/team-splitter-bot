package com.max.team.splitter.core.strategy;

import com.max.team.splitter.persistence.entities.PlayerEntity;

import java.util.List;

public interface TeamSplitStrategy {
    List<List<PlayerEntity>> split(int numberOfTeams, List<PlayerEntity> playerScores);

    SplitterStrategyType getType();
}
