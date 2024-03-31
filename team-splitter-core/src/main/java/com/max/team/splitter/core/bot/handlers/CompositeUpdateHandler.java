package com.max.team.splitter.core.bot.handlers;

import com.max.team.splitter.core.model.telegram.Update;

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
