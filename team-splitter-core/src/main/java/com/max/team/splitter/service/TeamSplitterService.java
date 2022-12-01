package com.max.team.splitter.service;

import com.max.team.splitter.model.Player;

import java.util.List;
import java.util.Map;

public interface TeamSplitterService {

    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players);

    public Map<String, List<Player>> splitTeams(int numberOfTeams, List<Player> players);
}
