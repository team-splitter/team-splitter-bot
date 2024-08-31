package com.max.team.splitter.core.service;

import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.PlayerEntity;

import java.util.List;
import java.util.Map;

public interface TeamSplitterService {

    public List<List<PlayerEntity>> splitIntoTeams(int numberOfTeams, SplitterStrategyType splitType, List<PlayerEntity> players);

    public Map<String, List<PlayerEntity>> splitTeams(int numberOfTeams, SplitterStrategyType splitType, List<PlayerEntity> players);
}
