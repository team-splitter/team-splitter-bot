package com.max.team.splitter.core;

import com.max.team.splitter.core.model.Player;

public class TestData {
    public static Player createPlayer(Long id) {
        Player player = new Player(id);
        return player;
    }
}
