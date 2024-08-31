package com.max.team.splitter.core.model;

import com.max.team.splitter.persistence.entities.PlayerEntity;

import java.util.List;

public class Team {
    private String name;
    private List<PlayerEntity> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerEntity> players) {
        this.players = players;
    }
}
