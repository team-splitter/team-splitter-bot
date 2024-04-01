package com.max.team.splitter.core;

import com.max.team.splitter.core.bot.handlers.CompositeUpdateHandler;
import com.max.team.splitter.core.bot.handlers.MessageUpdateHandler;
import com.max.team.splitter.core.bot.handlers.PollAnswerUpdateHandler;
import com.max.team.splitter.core.bot.handlers.PollUpdateHandler;
import com.max.team.splitter.persistence.PersistenceConfig;
import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@Import({
        PersistenceConfig.class,
})
@ComponentScan
@EnableScheduling()
public class TeamSplitterCoreConfig {

    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String botToken) {
        return new TelegramBot.Builder(botToken)
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

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
