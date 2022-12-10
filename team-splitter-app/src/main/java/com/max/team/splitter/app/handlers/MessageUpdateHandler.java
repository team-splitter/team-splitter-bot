package com.max.team.splitter.app.handlers;

import com.max.team.splitter.app.handlers.command.BotCommandHandler;
import com.max.team.splitter.core.service.PollService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.StopPoll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MessageUpdateHandler  implements UpdateHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final TelegramBot bot;
    private final PollService pollService;
    private final Set<Long> allowedUsers;

    private final List<BotCommandHandler> commandHandlers;

    public MessageUpdateHandler(TelegramBot bot, PollService pollService,
                                List<BotCommandHandler> commandHandlers,
                                @Value("${command.allowed-users}") String allowedUsers) {
        this.bot = bot;
        this.pollService = pollService;
        this.commandHandlers = commandHandlers;
        this.allowedUsers = allowedUsers.isBlank() ? Collections.emptySet() : Arrays.stream(allowedUsers.split(",")).map(Long::parseLong).collect(Collectors.toSet());
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        if (message == null) {
            return;
        }

        log.info("Handling message, {}", message);

        Long fromUserId = message.from().id();
        if (!allowedUsers.contains(fromUserId)) {
            log.info("User {} not allowed to execute command", fromUserId);
            return;
        }
        if (update.message().text() != null) {
            String text = update.message().text().trim();
            MessageEntity[] entities = message.entities();
            if (entities == null) {
                return;
            }
            List<MessageEntity> botCommands = Arrays.stream(entities).filter((item) -> item.type() == MessageEntity.Type.bot_command).collect(Collectors.toList());
            log.info("Bot commands {}", botCommands);

            for (MessageEntity botCommand : botCommands) {
                for (BotCommandHandler commandHandler : commandHandlers) {
                    String commandStr = text.substring(botCommand.offset(), botCommand.offset() + botCommand.length());
                    BotCommand command = BotCommand.getCommand(commandStr);
                    log.info("Got BotCommand={} by command str={}", command, commandStr);
                    Set<BotCommand> supports = commandHandler.supports();
                    if (command != null && supports.contains(command)) {
                        commandHandler.handle(message, text, botCommand);
                    }
                }
            }

//            String text = update.message().text().trim();
            log.info("text message = {}", text);
            Long chatId = update.message().chat().id();

            if (text.startsWith("/poll")) {

            } else if (text.startsWith("/split")) {



            } else if (text.toLowerCase().contains("monika")) {
                bot.execute(new SendMessage(chatId, "happy xmas"));
            } else if (text.startsWith("/closepoll")) {
                Optional<Integer> lastPollMessageId = pollService.getLastPollMessageId(chatId);
                if (lastPollMessageId.isPresent()) {
                    bot.execute(new StopPoll(chatId, lastPollMessageId.get()));
                } else {
                    log.warn("No last poll message id found");
                }
            }
        }

    }
}
