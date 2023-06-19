package com.max.team.splitter.core.model;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;


public class Game {
    private Long id;
    private String pollId;
    private Instant creationTime;

    private Integer teamSize;
    private Integer blueScored;
    private Integer redScored;

    private List<Team> teams = new LinkedList<>();

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

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Integer getBlueScored() {
        return blueScored;
    }

    public void setBlueScored(Integer blueScored) {
        this.blueScored = blueScored;
    }

    public Integer getRedScored() {
        return redScored;
    }

    public void setRedScored(Integer redScored) {
        this.redScored = redScored;
    }
}
