package com.max.team.splitter;

import com.max.team.splitter.handlers.CompositeUpdateHandler;
import com.max.team.splitter.handlers.MessageUpdateHandler;
import com.max.team.splitter.handlers.PollAnswerUpdateHandler;
import com.max.team.splitter.handlers.PollUpdateHandler;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;


@ComponentScan
public class TeamSplitterConfig {


    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String botToken) {
        return new TelegramBot.Builder(botToken)
//                .debug()
                .build();
    }

    @Bean
    public CompositeUpdateHandler compositeUpdateHandler(MessageUpdateHandler messageUpdateHandler,
                                                         PollAnswerUpdateHandler pollAnswerUpdateHandler,
                                                         PollUpdateHandler pollUpdateHandler
    ) {
        return new CompositeUpdateHandler(List.of(messageUpdateHandler,
                pollAnswerUpdateHandler,
                pollUpdateHandler));
    }
}
