package com.max.team.splitter.core.bot.service;

import com.max.team.splitter.core.bot.handlers.CompositeUpdateHandler;
import com.max.team.splitter.core.converter.AppConverters;
import com.max.team.splitter.core.model.GameSplit;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.core.model.telegram.Update;
import com.max.team.splitter.core.service.GameSplitService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPoll;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramBotService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PollService pollService;
    private final GameSplitService gameSplitService;

    private final TelegramBot bot;

    public TelegramBotService(PollService pollService, GameSplitService gameSplitService, TelegramBot bot) {
        this.pollService = pollService;
        this.gameSplitService = gameSplitService;
        this.bot = bot;
    }

    public void split(String pollId, int numberOfTeams, SplitterStrategyType splitType) {
        PollModel poll = pollService.getById(pollId);
        Long chatId = poll.getChatId();

        GameSplit gameSplit = gameSplitService.split(pollId, numberOfTeams, splitType);

        String textMessage = createTeamSplitMessage(gameSplit);

        log.info("Sending message text={} to chatId={}", textMessage, chatId);
        SendMessage request = new SendMessage(chatId, textMessage);
        request.parseMode(ParseMode.MarkdownV2);
        SendResponse sendResponse = bot.execute(request);
        log.info("SendMessage response={}", sendResponse);
    }

    private String createTeamSplitMessage(GameSplit gameSplit) {
        String textMessage = "";
        for (Team team : gameSplit.getTeams()) {
            String teamColor = team.getName();
            List<PlayerEntity> players = team.getPlayers();

            //Apply sorting by first name
            sortTeamByFirstName(players);

            textMessage += "*Team " + teamColor + "*\n";
            for (PlayerEntity player : players) {
                String playerNameRow = playerDisplayName(player);

                textMessage += playerNameRow + "\n";
            }
            textMessage += "\n";
        }
        return textMessage;
    }

    private String playerDisplayName(PlayerEntity player) {
        String personName = "";
        if (player.getFirstName() != null) {
            personName += player.getFirstName();
        }
        if (player.getLastName() != null) {

            personName += (personName.length() == 0 ? "" : " ") + player.getLastName();
        }

        return personName;
    }

    private static void sortTeamByFirstName(List<PlayerEntity> team) {
        team.sort((a, b) -> {
            String f1 = a.getFirstName();
            String f2 = b.getFirstName();
            if (f1 != null && f2 != null) {
                return f1.compareTo(f2);
            } else if (f1 != null )  {
                return 1;
            } else if ( f2 != null) {
                return -1;
            }else {
                return 0;
            }
        });
    }

    public PollModel createPoll(long chatId, String message) {
        SendPoll sendPoll = new SendPoll(chatId, message, "+", "-");
        sendPoll.isAnonymous(false);

        SendResponse sendPollResponse = bot.execute(sendPoll);
        log.info("Send poll response={}", sendPollResponse);
        PollModel pollModel = AppConverters.toPollModel(sendPollResponse.message().poll(), sendPollResponse.message().messageId(), chatId);
        pollService.addPoll(pollModel);

        return pollModel;
    }
}
