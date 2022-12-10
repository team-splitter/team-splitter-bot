package com.max.team.splitter.server;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.*;

public class TeamSplitterServer {

    static {
        if (System.getProperty("logging.config") == null) {
            System.setProperty("logging.config", "classpath:team-splitter-server-logback.xml");
        }
    }

    private static final String SPRING_CONFIG_NAME = "spring.config.name";
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SPRING_JMX_DEFAULT = "spring.jmx.default";
    private static final String SPRING_JMX_ENABLED = "spring.jmx.enabled";
    private static final String APPLICATION_NAME = "team-splitter-server";

    private final SpringApplication springApplication;
    private final Properties overrides = new Properties();
    private ConfigurableApplicationContext context;
    private final List<String> arguments = new ArrayList<>();
    private boolean disableJmx = false;

    public TeamSplitterServer() {
        this.springApplication = new SpringApplication(TeamSplitterServerConfig.class);
        springApplication.addInitializers(applicationContext -> applicationContext
                .getEnvironment()
                .getPropertySources()
                .addFirst(new PropertiesPropertySource("overrides", overrides)));
    }

    public TeamSplitterServer start() {
        initializeSystemProperties();
        String[] args = new String[arguments.size()];
        args = arguments.toArray(args);
        this.context = springApplication.run(args);
        clearSystemProperties();
        return this;
    }

    public TeamSplitterServer stop() {
        context.close();
        return this;
    }

    private void initializeSystemProperties() {
        System.setProperty(SPRING_CONFIG_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_APPLICATION_NAME, APPLICATION_NAME);
        System.setProperty(SPRING_JMX_DEFAULT, APPLICATION_NAME);
        if (disableJmx) {
            System.setProperty(SPRING_JMX_ENABLED, "false");
        }
    }

    private void clearSystemProperties() {
        System.clearProperty(SPRING_CONFIG_NAME);
        System.clearProperty(SPRING_APPLICATION_NAME);
        System.clearProperty(SPRING_JMX_DEFAULT);
        if (disableJmx) {
            System.clearProperty(SPRING_JMX_ENABLED);
        }
    }

    public TeamSplitterServer disableJmx() {
        this.disableJmx = true;
        return this;
    }

    public TeamSplitterServer withArguments(String... args) {
        arguments.addAll(Arrays.asList(args));
        return this;
    }

    public TeamSplitterServer withProperty(String key, String value) {
        this.overrides.setProperty(key, value);
        return this;
    }

    public TeamSplitterServer withRandomPort() {
        return withProperty("server.port", "0");
    }

    public Optional<ApplicationContext> getContext() {
        return Optional.ofNullable(context);
    }

    public static void main(String[] args) {
        new TeamSplitterServer().withArguments(args).start();
    }

    public int getServerPort() {
        Integer serverPort = context.getEnvironment().getProperty("local.server.port", Integer.class);
        if (serverPort == null) {
            throw new RuntimeException("Server not started!");
        }
        return serverPort;
    }
}
