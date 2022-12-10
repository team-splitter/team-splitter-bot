package com.max.team.splitter.app.handlers;

import com.max.team.splitter.app.converter.AppConverters;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.PollService;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PollAnswerUpdateHandler  implements UpdateHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PollService pollService;
    private final PlayerService playerService;


    public PollAnswerUpdateHandler(PollService pollService, PlayerService playerService) {
        this.pollService = pollService;
        this.playerService = playerService;
    }

    @Override
    public void handle(Update update) {
        PollAnswer pollAnswer = update.pollAnswer();
        if (pollAnswer == null) {
            return;
        }

        log.info("Handling poll answer update, {}", pollAnswer);

        pollService.addPollAnswer(AppConverters.toPollAnswerModel(pollAnswer));
        Player player = AppConverters.toPlayer(pollAnswer.user());
        playerService.addPlayer(player);
    }
}
