package com.max.team.splitter.bot.service;

import com.max.team.splitter.bot.converter.AppConverters;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.service.GameScheduleService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.persistence.entities.GameSchedule;
import com.max.team.splitter.persistence.entities.GameScheduleStatus;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPoll;
import com.pengrad.telegrambot.request.StopPoll;
import com.pengrad.telegrambot.response.PollResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class GameScheduler {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private volatile boolean isStarted = false;

    @Autowired
    private GameScheduleService gameScheduleService;
    @Autowired
    private PollService pollService;
    @Autowired
    private BotTeamSplitService botTeamSplitService;
    @Autowired
    private TelegramBot bot;
    @Value("${game.scheduler.task.periodInSeconds}")
    private long taskPeriodInSeconds;
    @Value("${game.scheduler.task.open_poll.enabled}")
    private boolean openPollEnabled;
    @Value("${game.scheduler.task.open_poll.lookForwardInDays}")
    private long openPollLookForwardInDays;
    @Value("${game.scheduler.task.close_poll.enabled}")
    private boolean closePollEnabled;
    @Value("${game.scheduler.task.close_poll.lookForwardInHours}")
    private long closePollLookForwardInHours;
    @Value("${game.scheduler.task.split_teams.enabled}")
    private boolean splitTeamsEnabled;
    @Value("${game.scheduler.task.split_teams.lookForwardInHours}")
    private long splitTeamsLookForwardInHours;

    @Value("${game.scheduler.chat_id}")
    private long chatId;

    private final Timer timer;

    public GameScheduler() {
        timer = new Timer();
    }

    public void start() {
        if (isStarted) {
            return;
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                log.info("Running Game scheduling task");
                List<GameSchedule> all = gameScheduleService.getAll();

                Instant now = Instant.now();

                List<GameSchedule> newSchedules = all
                        .stream().filter(schedule -> schedule.getStatus() == GameScheduleStatus.CREATED)
                        .filter(schedule -> schedule.getDate().isAfter(now) && schedule.getDate().isBefore(now.plus(openPollLookForwardInDays, ChronoUnit.DAYS)))
                        .collect(Collectors.toList());
                List<GameSchedule> openPollSchedules = all
                        .stream().filter(schedule -> schedule.getStatus() == GameScheduleStatus.POLL_OPENED)
                        .filter(schedule -> schedule.getDate().isAfter(now) && schedule.getDate().isBefore(now.plus(closePollLookForwardInHours, ChronoUnit.HOURS)))
                        .collect(Collectors.toList());
                List<GameSchedule> closedPollSchedules = all
                        .stream().filter(schedule -> schedule.getStatus() == GameScheduleStatus.POLL_CLOSED)
                        .filter(schedule -> schedule.getDate().isAfter(now) && schedule.getDate().isBefore(now.plus(splitTeamsLookForwardInHours, ChronoUnit.HOURS)))
                        .collect(Collectors.toList());

                if (openPollEnabled) {
                    handleOpenPollTask(newSchedules);
                }
                if (closePollEnabled) {
                    handleClosePollTask(openPollSchedules);
                }

                if (splitTeamsEnabled) {
                    handleSplitTeamsTask(closedPollSchedules);
                }

                log.info("Game scheduling task finished");

            }
        }, 1_000, TimeUnit.SECONDS.toMillis(taskPeriodInSeconds));
        isStarted = true;
    }

    private void handleSplitTeamsTask(List<GameSchedule> closedPollSchedules) {
        Optional<GameSchedule> first = closedPollSchedules.stream().findFirst();

        if (first.isPresent()) {
            GameSchedule gameSchedule = first.get();
            String pollId = gameSchedule.getPollId();
            log.info("Splitting teams for pollId={}", pollId);

            int numberOfTeams = 2; //so far default
            botTeamSplitService.split(pollId, numberOfTeams);

            gameSchedule.setStatus(GameScheduleStatus.COMPLETED);
            gameScheduleService.update(gameSchedule);
        }
    }

    private void handleClosePollTask(List<GameSchedule> openPollSchedules) {
        Optional<GameSchedule> first = openPollSchedules.stream().findFirst();

        if (first.isPresent()) {
            GameSchedule gameSchedule = first.get();

            log.info("Closing poll id {}", gameSchedule.getPollId());

            PollModel poll = pollService.getById(gameSchedule.getPollId());
            Long chatId = poll.getChatId();

            SendMessage message = new SendMessage(chatId, "Poll is closed now");
            SendResponse sendMessageResponse = bot.execute(message);
            log.info("Send message response={}", sendMessageResponse);


            log.info("Stopping poll chatId={}, messageId={}", chatId, poll.getMessageId());
            StopPoll request = new StopPoll(chatId, poll.getMessageId());
            PollResponse pollStopResponse = bot.execute(request);

            log.info("Stop poll response={}", pollStopResponse);
            gameSchedule.setStatus(GameScheduleStatus.POLL_CLOSED);
            gameScheduleService.update(gameSchedule);
        }
    }

    private void handleOpenPollTask(List<GameSchedule> newSchedules) {
        Optional<GameSchedule> first = newSchedules.stream().findFirst();

        if (first.isPresent()) {
            GameSchedule gameSchedule = first.get();

            log.info("Sending poll for gate at {} at location {}", gameSchedule.getDate(), gameSchedule.getLocation());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE (MM-dd-yyyy) 'at' hh:mm aa");
            sdf.setTimeZone(TimeZone.getTimeZone("EST"));
            String estDate = sdf.format(Date.from(gameSchedule.getDate()));

            String message = MessageFormat.format("Game is on {0} at {1}.", estDate, gameSchedule.getLocation());

            SendPoll sendPoll = new SendPoll(chatId, message, "+", "-");
            sendPoll.isAnonymous(false);

            SendResponse sendPollResponse = bot.execute(sendPoll);
            log.info("Send poll response={}", sendPollResponse);
            PollModel pollModel = AppConverters.toPollModel(sendPollResponse.message().poll(), sendPollResponse.message().messageId(), chatId);
            pollService.addPoll(pollModel);
            gameSchedule.setStatus(GameScheduleStatus.POLL_OPENED);
            gameSchedule.setPollId(pollModel.getId());
            gameScheduleService.update(gameSchedule);
        }
    }

    public void stop() {
        timer.cancel();
    }


}
