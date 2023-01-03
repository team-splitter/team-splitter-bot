package com.max.team.splitter.bot.converter;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PollAnswerModel;
import com.max.team.splitter.core.model.PollModel;
import com.pengrad.telegrambot.model.Poll;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.User;

public class AppConverters {
    public static Player toPlayer(User user) {
        return new Player(user.id(), user.firstName(), user.lastName(), user.username());
    }

    public static PollModel toPollModel(Poll poll, Integer messageId, Long chatId) {
        PollModel model = new PollModel();
        model.setId(poll.id());
        model.setQuestion(poll.question());
        model.setMessageId(messageId);
        model.setChatId(chatId);
        return model;
    }

    public static PollAnswerModel toPollAnswerModel(PollAnswer pollAnswer) {
        PollAnswerModel model = new PollAnswerModel();
        model.setPollId(pollAnswer.pollId());
        model.setPlayerId(pollAnswer.user().id());
        model.setOptionIds(pollAnswer.optionIds());
        return model;
    }
}
