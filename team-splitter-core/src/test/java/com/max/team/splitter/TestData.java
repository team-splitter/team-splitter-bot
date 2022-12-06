package com.max.team.splitter;

import com.max.team.splitter.model.Player;

public class TestData {
    public static Player createPlayer(Long id) {
        Player player = new Player(id);
        return player;
    }
}
