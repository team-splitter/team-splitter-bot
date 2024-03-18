package com.max.team.splitter.persistence.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game_split")
public class GameSplitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "poll_id")
    private String pollId;
    @Column(name = "creation_ts")
    private Instant creationTime;

    @Column(name = "team_size")
    private Integer teamSize;

    @Column(name = "split_alg")
    private String splitAlg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public String getSplitAlg() {
        return splitAlg;
    }

    public void setSplitAlg(String splitAlg) {
        this.splitAlg = splitAlg;
    }
}
