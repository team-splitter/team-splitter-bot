package com.max.team.splitter.core.model;

public class PlayerScore {
        private Player player;
        private int score;

        public PlayerScore(Player player, int score) {
            this.player = player;
            this.score = score;
        }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}