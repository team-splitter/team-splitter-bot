package com.max.team.splitter.strategy;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.model.PlayerScore;

import java.util.List;

public interface TeamSplitStrategy {
    List<List<Player>> split(int numberOfTeams, List<PlayerScore> playerScores);
}
