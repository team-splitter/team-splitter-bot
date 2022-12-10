package com.max.team.splitter.core.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;


public class Game {
    private Long id;
    private String pollId;
    private Instant creationTime;
    private Map<String, List<Player>> teams;

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
}
