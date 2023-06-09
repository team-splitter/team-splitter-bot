package com.max.team.splitter.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class PlayerStatEntity {
    @Id
    @Column(name = "player_id")
    private Long playerId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "total_win")
    private Integer totalWin;

    @Column(name = "total_loss")
    private Integer totalLoss;

    @Column(name = "total_draw")
    private Integer totalDraw;

    @Column(name = "total_games")
    private Integer totalGames;


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
}
