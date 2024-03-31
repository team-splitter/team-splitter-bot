package com.max.team.splitter.core.bot.handlers.command;

import com.max.team.splitter.core.bot.handlers.BotCommand;
import com.max.team.splitter.core.model.telegram.Message;
import com.max.team.splitter.core.model.telegram.MessageEntity;

import java.util.Set;

public interface BotCommandHandler {

    void handle(Message message, String text, MessageEntity botCommandEntity);

    Set<BotCommand> supports();
}
