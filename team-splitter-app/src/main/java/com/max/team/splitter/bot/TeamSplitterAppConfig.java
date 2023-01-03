package com.max.team.splitter.bot;

import com.max.team.splitter.bot.handlers.CompositeUpdateHandler;
import com.max.team.splitter.bot.handlers.MessageUpdateHandler;
import com.max.team.splitter.bot.handlers.PollAnswerUpdateHandler;
import com.max.team.splitter.bot.handlers.PollUpdateHandler;
import com.max.team.splitter.core.TeamSplitterCoreConfig;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;


@Import(value = {
        TeamSplitterCoreConfig.class
})
public class TeamSplitterAppConfig {

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
