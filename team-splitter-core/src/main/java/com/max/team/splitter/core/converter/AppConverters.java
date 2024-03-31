package com.max.team.splitter.core.converter;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PollAnswerModel;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.model.telegram.PollAnswer;
import com.max.team.splitter.core.model.telegram.User;
import com.pengrad.telegrambot.model.Poll;

public class AppConverters {
    public static Player toPlayer(User user) {
        return new Player(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());
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
        model.setPollId(pollAnswer.getPoll_id());
        model.setPlayerId(pollAnswer.getUser().getId());
        model.setOptionIds(pollAnswer.getOption_ids());
        return model;
    }
}
