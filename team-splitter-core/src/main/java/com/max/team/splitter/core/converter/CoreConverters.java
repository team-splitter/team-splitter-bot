package com.max.team.splitter.core.converter;

import com.max.team.splitter.core.model.*;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.*;

import java.time.Instant;
import java.util.List;

public class CoreConverters {


    public static Player toPlayer(PlayerEntity entity) {
        Player player = new Player(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getUserName());
        player.setScore(entity.getScore());
        player.setGameScore(entity.getGameScore());
        player.setPrivacy(entity.getPrivacy());
        return player;
    }

    public static PlayerEntity toPlayerEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId());
        entity.setFirstName(player.getFirstName());
        entity.setLastName(player.getLastName());
        entity.setUserName(player.getUsername());
        entity.setScore(player.getScore());
        entity.setPrivacy(player.getPrivacy());

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
        game.setTeamSize(entity.getTeamSize());
        game.setGameSplitId(entity.getGameSplitId());
        game.setTeamOneName(entity.getTeamOneName());
        game.setTeamTwoName(entity.getTeamTwoName());
        game.setTeamOneScored(entity.getTeamOneScored());
        game.setTeamTwoScored(entity.getTeamTwoScored());
        return game;
    }

    public static PollAnswerModel toPollAnswerModel(PollAnswerEntity entity) {
        PollAnswerModel model = new PollAnswerModel();
        model.setId(entity.getId());
        model.setPlayerId(entity.getPlayerId());
        return model;
    }

    public static PlayerStat toPlayerStat(PlayerStatEntity playerStatEntity) {
        PlayerStat model = new PlayerStat();
        //map all fields
        model.setPlayerId(playerStatEntity.getPlayerId());
        model.setFirstName(playerStatEntity.getFirstName());
        model.setLastName(playerStatEntity.getLastName());
        model.setTotalWin(playerStatEntity.getTotalWin());
        model.setTotalLoss(playerStatEntity.getTotalLoss());
        model.setTotalDraw(playerStatEntity.getTotalDraw());
        model.setTotalGames(playerStatEntity.getTotalGames());
        return model;
    }

    public static GameSplit toGameSplit(GameSplitEntity entity, List<Game> games, List<Team> teams) {
        GameSplit gameSplit = new GameSplit();
        gameSplit.setId(entity.getId());
        gameSplit.setPollId(entity.getPollId());
        gameSplit.setTeamSize(entity.getTeamSize());
        gameSplit.setSplitAlg(SplitterStrategyType.valueOf(entity.getSplitAlg()));
        gameSplit.setGames(games);
        gameSplit.setTeams(teams);
        return gameSplit;
    }
}
