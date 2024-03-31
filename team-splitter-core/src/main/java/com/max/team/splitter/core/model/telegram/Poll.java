package com.max.team.splitter.core.model.telegram;

import com.pengrad.telegrambot.model.PollOption;

public class Poll {
    private String id;
    private String question;
    private PollOption[] options;
    private Integer total_voter_count;
    private Boolean is_closed;
    private Boolean is_anonymous;
    private String type;
    private Boolean allows_multiple_answers;
    private Integer correct_option_id;
    private String explanation;
    private MessageEntity[] explanation_entities;
    private Integer open_period;
    private Integer close_date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public PollOption[] getOptions() {
        return options;
    }

    public void setOptions(PollOption[] options) {
        this.options = options;
    }

    public Integer getTotal_voter_count() {
        return total_voter_count;
    }

    public void setTotal_voter_count(Integer total_voter_count) {
        this.total_voter_count = total_voter_count;
    }

    public Boolean getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(Boolean is_closed) {
        this.is_closed = is_closed;
    }

    public Boolean getIs_anonymous() {
        return is_anonymous;
    }

    public void setIs_anonymous(Boolean is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAllows_multiple_answers() {
        return allows_multiple_answers;
    }

    public void setAllows_multiple_answers(Boolean allows_multiple_answers) {
        this.allows_multiple_answers = allows_multiple_answers;
    }

    public Integer getCorrect_option_id() {
        return correct_option_id;
    }

    public void setCorrect_option_id(Integer correct_option_id) {
        this.correct_option_id = correct_option_id;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public MessageEntity[] getExplanation_entities() {
        return explanation_entities;
    }

    public void setExplanation_entities(MessageEntity[] explanation_entities) {
        this.explanation_entities = explanation_entities;
    }

    public Integer getOpen_period() {
        return open_period;
    }

    public void setOpen_period(Integer open_period) {
        this.open_period = open_period;
    }

    public Integer getClose_date() {
        return close_date;
    }

    public void setClose_date(Integer close_date) {
        this.close_date = close_date;
    }
}
