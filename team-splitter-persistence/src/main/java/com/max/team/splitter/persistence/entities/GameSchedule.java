package com.max.team.splitter.persistence.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game_schedule")
public class GameSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "location")
    private String location;
    @Column(name = "date")
    private Instant date;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private GameScheduleStatus status = GameScheduleStatus.CREATED;

    @Column(name = "poll_id")
    private String pollId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public GameScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(GameScheduleStatus status) {
        this.status = status;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }
}
