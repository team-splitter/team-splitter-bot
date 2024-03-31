package com.max.team.splitter.core.model.telegram;


public class PollAnswer {
    private String poll_id;
    private User user;
    private Integer[] option_ids;

    public String getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(String poll_id) {
        this.poll_id = poll_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer[] getOption_ids() {
        return option_ids;
    }

    public void setOption_ids(Integer[] option_ids) {
        this.option_ids = option_ids;
    }
}
