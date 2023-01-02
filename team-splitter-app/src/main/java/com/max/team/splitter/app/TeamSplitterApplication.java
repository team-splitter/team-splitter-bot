package com.max.team.splitter.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Properties;

@SpringBootApplication
@Import({
        TeamSplitterAppConfig.class,
})
public class TeamSplitterApplication  implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TeamSplitterApplication.class);
    private static final String SPRING_CONFIG_NAME = "spring.config.name";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String APPLICATION_NAME = "team-splitter-bot";

    private final Properties overrides = new Properties();

    @Autowired
    private TeamSplitterBot teamSplitterBot;
    public static void main(String[] args) throws Exception {

        System.setProperty(SPRING_CONFIG_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_APPLICATION_NAME, APPLICATION_NAME);

        //disabled banner, don't want to see the spring logo
        SpringApplication app = new SpringApplication(TeamSplitterApplication.class);

        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Running TeamSplitterApplication");
        teamSplitterBot.start();

    }
}
