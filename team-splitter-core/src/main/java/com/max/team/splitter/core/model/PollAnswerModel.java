package com.max.team.splitter.core.model;

public class PollAnswerModel {
    private Long id;
    private String pollId;
    private Integer[] optionIds;
    private Long playerId;

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

    public Integer[] getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(Integer[] optionIds) {
        this.optionIds = optionIds;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
