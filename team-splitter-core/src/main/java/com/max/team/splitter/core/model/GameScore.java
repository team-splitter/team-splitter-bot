package com.max.team.splitter.core.model;

public class GameScore {
    private String teamOneName;
    private String teamTwoName;
    private Integer teamOneScored;
    private Integer teamTwoScored;

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
}
