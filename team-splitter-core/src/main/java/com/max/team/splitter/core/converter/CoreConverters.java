package com.max.team.splitter.core.converter;

import com.max.team.splitter.core.model.Game;
import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PollAnswerModel;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import com.max.team.splitter.persistence.entities.PollEntity;

import java.time.Instant;

public class CoreConverters {


    public static Player toPlayer(PlayerEntity entity) {
        Player player = new Player(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getUserName());
        player.setScore(entity.getScore());
        return player;
    }

    public static PlayerEntity toPlayerEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId());
        entity.setFirstName(player.getFirstName());
        entity.setLastName(player.getLastName());
        entity.setUserName(player.getUsername());
        entity.setScore(player.getScore());

        return entity;
    }

    public static PollEntity toPollEntity(PollModel poll) {
        PollEntity entity = new PollEntity();
        entity.setId(poll.getId());
        entity.setQuestion(poll.getQuestion());
        entity.setCreationTimestamp(Instant.now());
        entity.setMessageId(poll.getMessageId());
        entity.setChatId(poll.getChatId());
        return entity;
    }

    public static PollModel toPollModel(PollEntity entity) {
        PollModel model = new PollModel();
        model.setId(entity.getId());
        model.setQuestion(entity.getQuestion());
        model.setChatId(entity.getChatId());
        model.setCreationTime(entity.getCreationTimestamp());
        model.setMessageId(entity.getMessageId());
        return model;
    }

    public static PollAnswerEntity toPollAnswerEntity(PollAnswerModel pollAnswer) {
        return toPollAnswerEntity(pollAnswer.getPollId(), pollAnswer.getPlayerId());
    }

    public static PollAnswerEntity toPollAnswerEntity(String pollId, Long playerId) {
        PollAnswerEntity entity = new PollAnswerEntity();
        entity.setPollId(pollId);
        entity.setCreationTimestamp(Instant.now());
        entity.setPlayerId(playerId);
        return entity;
    }

    public static Game toGame(GameEntity entity) {
        Game game = new Game();
        game.setId(entity.getId());
        game.setPollId(entity.getPollId());
        game.setCreationTime(entity.getCreationTimestamp());
        game.setBlueScored(entity.getBlueScored());
        game.setRedScored(entity.getRedScored());
        return game;
    }

    public static PollAnswerModel toPollAnswerModel(PollAnswerEntity entity) {
        PollAnswerModel model = new PollAnswerModel();
        model.setId(entity.getId());
        model.setPlayerId(entity.getPlayerId());
        return model;
    }
}
