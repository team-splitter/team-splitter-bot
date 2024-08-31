package com.max.team.splitter.server.dto;

import com.max.team.splitter.persistence.entities.PlayerEntity;

public class PollVote {
    private Long id;
    private PlayerEntity player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
