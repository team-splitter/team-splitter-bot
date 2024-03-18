package com.max.team.splitter.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "team_entry")
public class TeamEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "game_split_id")
    private Long gameSplitId;
    @Column(name = "team_name")
    private String teamName;
    @Column(name = "player_id")
    private Long playerId;
    @Column(name = "score")
    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameSplitId() {
        return gameSplitId;
    }

    public void setGameSplitId(Long gameSplitId) {
        this.gameSplitId = gameSplitId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
