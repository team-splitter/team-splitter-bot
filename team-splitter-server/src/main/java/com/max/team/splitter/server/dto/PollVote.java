package com.max.team.splitter.server.dto;

import com.max.team.splitter.core.model.Player;

public class PollVote {
    private Long id;
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
