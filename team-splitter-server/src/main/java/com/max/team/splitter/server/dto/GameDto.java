package com.max.team.splitter.server.dto;

import java.time.Instant;
import java.util.List;

public class GameDto {
    private Long id;
    private String pollId;
    private Instant creationTime;

    private Integer blueScored;
    private Integer redScored;
    private List<TeamDto> teams;

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

    public List<TeamDto> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDto> teams) {
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
