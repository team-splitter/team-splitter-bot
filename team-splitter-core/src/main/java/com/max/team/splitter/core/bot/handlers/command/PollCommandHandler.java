package com.max.team.splitter.core.bot.handlers.command;

import com.max.team.splitter.core.bot.handlers.BotCommand;
import com.max.team.splitter.core.converter.AppConverters;
import com.max.team.splitter.core.service.PollService;
import com.pengrad.telegrambot.TelegramBot;
import com.max.team.splitter.core.model.telegram.Message;
import com.max.team.splitter.core.model.telegram.MessageEntity;
import com.pengrad.telegrambot.request.SendPoll;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PollCommandHandler implements BotCommandHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PollService pollService;
    private final TelegramBot bot;

    public PollCommandHandler(PollService pollService, TelegramBot bot) {
        this.pollService = pollService;
        this.bot = bot;
    }

    @Override
    public void handle(Message message, String text, MessageEntity botCommandEntity) {
        log.info("Handling poll command");
        String pollTitle = text.substring(botCommandEntity.getOffset()+botCommandEntity.getLength()).trim();
        pollTitle = pollTitle.isEmpty() ? "Game" : pollTitle;
        Long chatId = message.getChat().getId();
        SendPoll sendPoll = new SendPoll(chatId, pollTitle, "+", "-");
        sendPoll.isAnonymous(false);
        SendResponse response = bot.execute(sendPoll);
        log.info("Send poll response={}", response);
        if (response.message() != null) {
            pollService.addPoll(AppConverters.toPollModel(response.message().poll(), response.message().messageId(), chatId));
        }
    }

    @Override
    public Set<BotCommand> supports() {
        return Set.of(BotCommand.POLL);
    }
}
