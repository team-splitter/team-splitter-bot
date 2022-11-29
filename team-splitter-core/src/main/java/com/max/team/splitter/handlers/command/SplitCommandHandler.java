package com.max.team.splitter.handlers.command;

import com.max.team.splitter.handlers.BotCommand;
import com.max.team.splitter.model.Player;
import com.max.team.splitter.service.PlayerService;
import com.max.team.splitter.service.PollService;
import com.max.team.splitter.service.TeamSplitterService;
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
import java.util.Optional;
import java.util.Set;

@Component
public class SplitCommandHandler implements BotCommandHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final PollService pollService;
    private final PlayerService playerService;
    private final TelegramBot bot;
    private final TeamSplitterService teamSplitterService;

    public SplitCommandHandler(PollService pollService, PlayerService playerService, TelegramBot bot, TeamSplitterService teamSplitterService) {
        this.pollService = pollService;
        this.playerService = playerService;
        this.bot = bot;
        this.teamSplitterService = teamSplitterService;
    }

    @Override
    public void handle(Message message, String text, MessageEntity botCommandEntity) {
        log.info("Handling split command");
        Optional<String> lastPollId = pollService.getLastPollId(message.chat().id());
        if (lastPollId.isEmpty()) return;

        List<Long> ids = pollService.getPlayerIdsGoingToGame(lastPollId.get());
        List<Player> players = playerService.getPlayersByIds(ids);

        log.info("Split players={}", players);
        List<List<Player>> teams = teamSplitterService.splitIntoTeams(2, players);

        String[] teamColors = new String[]{"Blue", "Red", "White", "Black"};

        String textMessage = "";
        int teamNum = 0;
        for (List<Player> team : teams) {
            String teamColor = teamColors[teamNum];

            textMessage += "Team " + teamColor + "\n";
            for (Player player : team) {
                if (player.getUsername() != null) {
                    textMessage += "@" + player.getUsername() + "\n";
                } else {
                    String personName = "";
                    if (player.getFirstName() != null) {
                        personName += player.getFirstName();
                    }
                    if (player.getLastName() != null) {

                        personName += (personName.length() == 0 ? " " : "") + player.getLastName();
                    }
                    textMessage += "[" + personName + "](tg://user?id=" + player.getId() + ") \n";

                }

            }
            teamNum++;
        }

//                textMessage = "test";
        Long chatId = message.chat().id();
        log.info("Sending message text={} to chatId={}", textMessage, chatId);
        SendMessage request = new SendMessage(chatId, textMessage);
        request.parseMode(ParseMode.MarkdownV2);
        SendResponse sendResponse = bot.execute(request);
        log.info("SendMessage response={}", sendResponse);
    }

    @Override
    public Set<BotCommand> supports() {
        return Set.of(BotCommand.SPLIT);
    }
}
