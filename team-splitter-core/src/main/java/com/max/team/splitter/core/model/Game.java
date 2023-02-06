package com.max.team.splitter.core.model;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Game {
    private Long id;
    private String pollId;
    private Instant creationTime;

    private Integer blueScored;
    private Integer redScored;
    private Map<String, List<Player>> teams = new LinkedHashMap<>();

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

    public Map<String, List<Player>> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, List<Player>> teams) {
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
