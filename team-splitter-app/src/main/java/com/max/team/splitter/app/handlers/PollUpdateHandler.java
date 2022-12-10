package com.max.team.splitter.app.handlers;

import com.pengrad.telegrambot.model.Poll;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PollUpdateHandler implements UpdateHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(Update update) {
        Poll poll = update.poll();
        if (poll == null) {
            return;
        }

        log.info("Handling poll update, poll={}", poll);


    }
}
