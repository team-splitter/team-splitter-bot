package com.max.team.splitter.app;

import com.max.team.splitter.app.handlers.CompositeUpdateHandler;
import com.max.team.splitter.app.handlers.MessageUpdateHandler;
import com.max.team.splitter.app.handlers.PollAnswerUpdateHandler;
import com.max.team.splitter.app.handlers.PollUpdateHandler;
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
