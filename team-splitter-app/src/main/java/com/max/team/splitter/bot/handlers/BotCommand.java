package com.max.team.splitter.bot.handlers;

public enum BotCommand {
    POLL("/poll"),
    SPLIT("/split"),
    CLOSE_POLL("/closepoll")
    ;

    private String command;

    BotCommand(String command) {
        this.command = command;
    }

    public static BotCommand getCommand(String value) {
        for (BotCommand botCommand : BotCommand.values()) {
            if (botCommand.command.equals(value)) {
                return botCommand;
            }
        }

        return null;
    }
}
