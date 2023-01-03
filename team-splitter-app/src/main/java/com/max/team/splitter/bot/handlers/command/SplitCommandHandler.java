package com.max.team.splitter.bot.handlers.command;

import com.max.team.splitter.bot.handlers.BotCommand;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.GameService;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.TeamSplitterService;
import com.max.team.splitter.core.service.PollService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class SplitCommandHandler implements BotCommandHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PollService pollService;
    private final PlayerService playerService;
    private final TelegramBot bot;
    private final TeamSplitterService teamSplitterService;
    private final GameService gameService;

    public SplitCommandHandler(PollService pollService, PlayerService playerService, TelegramBot bot, TeamSplitterService teamSplitterService, GameService gameService) {
        this.pollService = pollService;
        this.playerService = playerService;
        this.bot = bot;
        this.teamSplitterService = teamSplitterService;
        this.gameService = gameService;
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
        List<Long> playerIds = pollService.getPlayerIdsGoingToGame(pollId);
        List<Player> players = playerService.getPlayersByIds(playerIds);

        int numberOfTeams = getNumberOfTeams(text, botCommandEntity);

        log.info("Split players={}", players);
        Map<String, List<Player>> teams = teamSplitterService.splitTeams(numberOfTeams, players);
        gameService.saveGameSplit(teams, pollId);


        String textMessage = "";
        for (Map.Entry<String, List<Player>> entry : teams.entrySet()) {
            String teamColor = entry.getKey();
            List<Player> team = entry.getValue();

            textMessage += "*Team " + teamColor + "*\n";
            for (Player player : team) {
                String playerNameRow = playerDisplayName(player);

                textMessage += playerNameRow + "\n";
            }
            textMessage += "\n";
        }

        Long chatId = message.chat().id();
        log.info("Sending message text={} to chatId={}", textMessage, chatId);
        SendMessage request = new SendMessage(chatId, textMessage);
        request.parseMode(ParseMode.MarkdownV2);
        SendResponse sendResponse = bot.execute(request);
        log.info("SendMessage response={}", sendResponse);
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

    @Override
    public Set<BotCommand> supports() {
        return Set.of(BotCommand.SPLIT);
    }
}