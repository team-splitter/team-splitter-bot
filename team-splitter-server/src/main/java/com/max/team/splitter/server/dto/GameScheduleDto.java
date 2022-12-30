package com.max.team.splitter.server.dto;

import com.max.team.splitter.persistence.entities.GameScheduleStatus;

import java.time.Instant;

public class GameScheduleDto {
    private Long id;
    private String location;
    private Instant date;

    private GameScheduleStatus status = GameScheduleStatus.CREATED;

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
}
