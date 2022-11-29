package com.max.team.splitter.handlers;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.service.PlayerService;
import com.max.team.splitter.service.PollService;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.max.team.splitter.converter.Converters.toPlayer;

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

        pollService.addPollAnswer(pollAnswer);

        User user = pollAnswer.user();
        Player player = toPlayer(user);
        playerService.addPlayer(player);
    }
}
