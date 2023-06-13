package com.max.team.splitter.core.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.strategy.SplitterStrategyType;

import java.util.List;
import java.util.Map;

public interface TeamSplitterService {

    public List<List<Player>> splitIntoTeams(int numberOfTeams, SplitterStrategyType splitType, List<Player> players);

    public Map<String, List<Player>> splitTeams(int numberOfTeams, SplitterStrategyType splitType, List<Player> players);
}
