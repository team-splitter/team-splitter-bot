package com.max.team.splitter.bot.handlers.command;

import com.max.team.splitter.bot.handlers.BotCommand;
import com.max.team.splitter.bot.service.BotTeamSplitService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class SplitCommandHandler implements BotCommandHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PollService pollService;

    private final BotTeamSplitService botTeamSplitService;

    public SplitCommandHandler(PollService pollService, BotTeamSplitService botTeamSplitService) {
        this.pollService = pollService;
        this.botTeamSplitService = botTeamSplitService;
        ;
    }

    @Override
    public void handle(Message message, String text, MessageEntity botCommandEntity) {
        log.info("Handling split command");
        Optional<String> lastPollId = pollService.getLastPollId(message.chat().id());
        if (lastPollId.isEmpty()) {
            log.warn("Last poll id is not found by chat_id={}", message.chat().id());
            return;
        }

        String pollId = lastPollId.get();
        log.info("Last poll_id={} found by chat_id={}", pollId, message.chat().id());

        int numberOfTeams = getNumberOfTeams(text, botCommandEntity);
        SplitterStrategyType splitType = getSplitterStrategyType(text, botCommandEntity);
        botTeamSplitService.split(pollId, numberOfTeams, splitType);
    }

    private SplitterStrategyType getSplitterStrategyType(String text, MessageEntity botCommandEntity) {
        SplitterStrategyType splitType = SplitterStrategyType.TEAM_SCORE_BALANCE;
        String[] tokens = text.substring(botCommandEntity.offset() + botCommandEntity.length()).trim().split(" ");
        if (tokens.length > 1) {
            String token = tokens[1];
            if ("cir".equals(token)) {
                splitType = SplitterStrategyType.CIRCULAR;
            } else if ("baf".equals(token)) {
                splitType = SplitterStrategyType.BACK_AND_FORCE;
            } else if ("team_score".equals(token)) {
                splitType = SplitterStrategyType.TEAM_SCORE_BALANCE;
            }
            log.info("Can't parse splitter strategy type by token={}", token);
        }
        return splitType;
    }

    private int getNumberOfTeams(String text, MessageEntity botCommandEntity) {
        int numberOfTeams = 2;

        String[] tokens = text.substring(botCommandEntity.offset() + botCommandEntity.length()).trim().split(" ");
        if (tokens.length > 0) {
            try {
                numberOfTeams = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                log.info("Can't get number of teams from tokens={}", tokens);
            }
        }

        return numberOfTeams;
    }

    @Override
    public Set<BotCommand> supports() {
        return Set.of(BotCommand.SPLIT);
    }
}
