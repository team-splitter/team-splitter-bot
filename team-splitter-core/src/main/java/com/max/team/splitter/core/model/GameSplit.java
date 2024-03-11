package com.max.team.splitter.core.model;

import com.max.team.splitter.core.strategy.SplitterStrategyType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameSplit {
    private Long id;
    private String pollId;
    private Integer teamSize;
    private SplitterStrategyType splitAlg;
    private Instant creationTime;

    private List<Team> teams = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public SplitterStrategyType getSplitAlg() {
        return splitAlg;
    }

    public void setSplitAlg(SplitterStrategyType splitAlg) {
        this.splitAlg = splitAlg;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
