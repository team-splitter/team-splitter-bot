package com.max.team.splitter.converter;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import com.max.team.splitter.persistence.entities.PollEntity;
import com.pengrad.telegrambot.model.Poll;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.User;

import java.time.Instant;

public class Converters {
    public static Player toPlayer(User user) {
        return new Player(user.id(), user.firstName(), user.lastName(), user.username());
    }

    public static Player toPlayer(PlayerEntity entity) {
        return new Player(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getUserName());
    }

    public static PlayerEntity toPlayerEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId());
        entity.setFirstName(player.getFirstName());
        entity.setLastName(player.getLastName());
        entity.setUserName(player.getUsername());

        return entity;
    }

    public static PollEntity toPollEntity(Poll poll, Integer messageId, Long chatId) {
        PollEntity entity = new PollEntity();
        entity.setId(poll.id());
        entity.setQuestion(poll.question());
        entity.setCreationTimestamp(Instant.now());
        entity.setMessageId(messageId);
        entity.setChatId(chatId);
        return entity;
    }

    public static PollAnswerEntity toPollAnswerEntity(PollAnswer pollAnswer) {
        PollAnswerEntity entity = new PollAnswerEntity();
        entity.setPollId(pollAnswer.pollId());
        entity.setCreationTimestamp(Instant.now());
        entity.setPlayerId(pollAnswer.user().id());
        return entity;
    }
}
