package com.max.team.splitter.core.model.telegram;

public class PollOption {
    private String text;
    private Integer voter_count;

    public PollOption(String text, Integer voter_count) {
        this.text = text;
        this.voter_count = voter_count;
    }
}
