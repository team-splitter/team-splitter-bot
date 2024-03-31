package com.max.team.splitter.core.service;

import com.max.team.splitter.core.bot.service.TelegramBotService;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.persistence.entities.GameSchedule;
import com.max.team.splitter.persistence.entities.GameScheduleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class GameScheduler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GameScheduleService gameScheduleService;
    private final TelegramBotService telegramBotService;

    @Value("${game.scheduler.task.open_poll.lookForwardInDays}")
    private long openPollLookForwardInDays;

    @Value("${game.scheduler.chat_id}")
    private long chatId;

    public GameScheduler(GameScheduleService gameScheduleService, TelegramBotService telegramBotService) {
        this.gameScheduleService = gameScheduleService;
        this.telegramBotService = telegramBotService;
    }

    @Scheduled(cron = "${game.scheduler.task.open_poll.cron}")
    public void openPollTask() {
        log.info("Running Open Poll scheduling task");
        List<GameSchedule> all = gameScheduleService.getAll();

        Instant now = Instant.now();

        List<GameSchedule> newSchedules = all
                .stream().filter(schedule -> schedule.getStatus() == GameScheduleStatus.CREATED)
                .filter(schedule -> schedule.getDate().isAfter(now) && schedule.getDate().isBefore(now.plus(openPollLookForwardInDays, ChronoUnit.DAYS)))
                .collect(Collectors.toList());

        handleOpenPollTask(newSchedules);
    }


    private void handleOpenPollTask(List<GameSchedule> newSchedules) {
        Optional<GameSchedule> first = newSchedules.stream().findFirst();

        if (first.isPresent()) {
            GameSchedule gameSchedule = first.get();

            log.info("Sending poll for game at {} at location {}", gameSchedule.getDate(), gameSchedule.getLocation());
            String message = createNewGamePollMessage(gameSchedule);

            PollModel pollModel = telegramBotService.createPoll(chatId, message);

            gameSchedule.setStatus(GameScheduleStatus.POLL_OPENED);
            gameSchedule.setPollId(pollModel.getId());
            gameScheduleService.update(gameSchedule);
            log.info("Poll created for game at {} at location {}", gameSchedule.getDate(), gameSchedule.getLocation());
        } else {
            log.info("No upcoming games found");
        }
    }

    private static String createNewGamePollMessage(GameSchedule gameSchedule) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE (MM-dd-yyyy) 'at' hh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String estDate = sdf.format(Date.from(gameSchedule.getDate()));

        return MessageFormat.format("Game is on {0} at {1}.", estDate, gameSchedule.getLocation());
    }

}
