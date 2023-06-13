package com.max.team.splitter.bot.service;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.service.GameService;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.core.service.TeamSplitterService;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.max.team.splitter.core.Constants.DEFAULT_SCORE;

@Service
public class BotTeamSplitService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PollService pollService;
    private final PlayerService playerService;
    private final TeamSplitterService teamSplitterService;
    private final GameService gameService;

    private final TelegramBot bot;

    public BotTeamSplitService(PollService pollService, PlayerService playerService, TeamSplitterService teamSplitterService, GameService gameService, TelegramBot bot) {
        this.pollService = pollService;
        this.playerService = playerService;
        this.teamSplitterService = teamSplitterService;
        this.gameService = gameService;
        this.bot = bot;
    }

    public void split(String pollId, int numberOfTeams, SplitterStrategyType splitType) {
        PollModel poll = pollService.getById(pollId);
        Long chatId = poll.getChatId();

        List<Long> playerIds = pollService.getPlayerIdsGoingToGame(pollId);
        List<Player> players = playerService.getPlayersByIds(playerIds);
        players.forEach(player -> player.setScore(player.getScore() != null ? player.getScore() : DEFAULT_SCORE));



        log.info("Split players={}", players);
        Map<String, List<Player>> teams = teamSplitterService.splitTeams(numberOfTeams, splitType, players);
        gameService.saveGameSplit(teams, pollId);


        String textMessage = "";
        for (Map.Entry<String, List<Player>> entry : teams.entrySet()) {
            String teamColor = entry.getKey();
            List<Player> team = entry.getValue();

            //Apply sorting by first name
            sortTeamByFirstName(team);

            textMessage += "*Team " + teamColor + "*\n";
            for (Player player : team) {
                String playerNameRow = playerDisplayName(player);

                textMessage += playerNameRow + "\n";
            }
            textMessage += "\n";
        }

        log.info("Sending message text={} to chatId={}", textMessage, chatId);
        SendMessage request = new SendMessage(chatId, textMessage);
        request.parseMode(ParseMode.MarkdownV2);
        SendResponse sendResponse = bot.execute(request);
        log.info("SendMessage response={}", sendResponse);
    }

    private String playerDisplayName(Player player) {
        String personName = "";
        if (player.getFirstName() != null) {
            personName += player.getFirstName();
        }
        if (player.getLastName() != null) {

            personName += (personName.length() == 0 ? "" : " ") + player.getLastName();
        }

        return personName;
    }

    private static void sortTeamByFirstName(List<Player> team) {
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
}
