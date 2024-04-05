package com.max.team.splitter.core.model;

public class PlayerStat {
    private Long playerId;

    private String firstName;

    private String lastName;

    private Integer totalWin;

    private Integer totalLoss;

    private Integer totalDraw;

    private Integer totalGames;

    private Integer totalDays;

    public PlayerStat() {
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Integer totalWin) {
        this.totalWin = totalWin;
    }

    public Integer getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(Integer totalLoss) {
        this.totalLoss = totalLoss;
    }

    public Integer getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(Integer totalDraw) {
        this.totalDraw = totalDraw;
    }

    public Integer getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(Integer totalGames) {
        this.totalGames = totalGames;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }
}
