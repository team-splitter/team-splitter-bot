package com.max.team.splitter.service;

import com.max.team.splitter.model.Player;

import java.util.List;

public interface TeamSplitterService {

    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players);
}
