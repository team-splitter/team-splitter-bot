package com.max.team.splitter.core.bot.handlers;

import com.max.team.splitter.core.model.telegram.Poll;
import com.max.team.splitter.core.model.telegram.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PollUpdateHandler implements UpdateHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(Update update) {
        Poll poll = update.getPoll();
        if (poll == null) {
            return;
        }

        log.info("Handling poll update, poll={}", poll);


    }
}
