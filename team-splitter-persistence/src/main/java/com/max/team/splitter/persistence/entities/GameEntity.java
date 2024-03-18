package com.max.team.splitter.persistence.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_ts")
    private Instant creationTimestamp;

    @Column(name = "team_one_name")
    private String teamOneName;

    @Column(name = "team_two_name")
    private String teamTwoName;

    @Column(name = "team_one_scored")
    private Integer teamOneScored;

    @Column(name = "team_two_scored")
    private Integer teamTwoScored;

    @Column(name = "game_split_id")
    private Long gameSplitId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }

    public Integer getTeamOneScored() {
        return teamOneScored;
    }

    public void setTeamOneScored(Integer teamOneScored) {
        this.teamOneScored = teamOneScored;
    }

    public Integer getTeamTwoScored() {
        return teamTwoScored;
    }

    public void setTeamTwoScored(Integer teamTwoScored) {
        this.teamTwoScored = teamTwoScored;
    }

    public Long getGameSplitId() {
        return gameSplitId;
    }

    public void setGameSplitId(Long gameSplitId) {
        this.gameSplitId = gameSplitId;
    }
}
