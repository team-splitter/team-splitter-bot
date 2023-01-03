package com.max.team.splitter.bot.handlers;

import com.pengrad.telegrambot.model.Update;

import java.util.List;

public class CompositeUpdateHandler implements UpdateHandler {

    private final List<UpdateHandler> handlers;

    public CompositeUpdateHandler(List<UpdateHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void handle(Update update) {
        for (UpdateHandler handler : handlers) {
            handler.handle(update);
        }
    }
}
