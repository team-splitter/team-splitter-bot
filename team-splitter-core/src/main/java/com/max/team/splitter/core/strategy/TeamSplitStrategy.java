package com.max.team.splitter.core.strategy;

import com.max.team.splitter.core.model.Player;

import java.util.List;

public interface TeamSplitStrategy {
    List<List<Player>> split(int numberOfTeams, List<Player> playerScores);

    SplitterStrategyType getType();
}
