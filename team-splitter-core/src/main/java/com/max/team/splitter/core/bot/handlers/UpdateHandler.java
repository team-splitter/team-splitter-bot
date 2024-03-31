package com.max.team.splitter.core.bot.handlers;


import com.max.team.splitter.core.model.telegram.Update;

public interface UpdateHandler {

    void handle(Update update);
}
