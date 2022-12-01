package com.max.team.splitter.service;

import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import com.max.team.splitter.persistence.entities.PollEntity;
import com.max.team.splitter.persistence.repositories.PollAnswerRepository;
import com.max.team.splitter.persistence.repositories.PollRepository;
import com.pengrad.telegrambot.model.Poll;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.max.team.splitter.converter.Converters.toPollAnswerEntity;
import static com.max.team.splitter.converter.Converters.toPollEntity;

@Service
public class PollService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;

    public PollService(PollRepository pollRepository, PollAnswerRepository pollAnswerRepository) {
        this.pollRepository = pollRepository;
        this.pollAnswerRepository = pollAnswerRepository;
    }

    public void addPoll(Poll poll, Integer messageId, Long chatId) {
        PollEntity entity = toPollEntity(poll, messageId, chatId);
        pollRepository.save(entity);
    }


    public void addPollAnswer(PollAnswer pollAnswer) {
        Integer[] optionIds = pollAnswer.optionIds();
        String pollId = pollAnswer.pollId();
        User user = pollAnswer.user();
        Long userId = user.id();

        if (optionIds.length == 0) {
            //retract vote
            log.info("retract vote, removing player by id={}", userId);
            Optional<PollAnswerEntity> existing = pollAnswerRepository.findByPollIdAndPlayerId(pollId, userId);
            if (existing.isPresent()) {
                log.info("Deleting existing poll answer with id={}", existing.get().getPollId());
                pollAnswerRepository.delete(existing.get());
            }
        } else if (optionIds[0] == 0) {
            //vote +
            log.info("adding player with id={}", userId);
            PollAnswerEntity entity = toPollAnswerEntity(pollAnswer);
            pollAnswerRepository.save(entity);
        }
    }

    public List<Long> getPlayerIdsGoingToGame(String pollId) {
        List<PollAnswerEntity> answers = pollAnswerRepository.findByPollId(pollId);
        List<Long> going = answers.stream().map(PollAnswerEntity::getPlayerId).collect(Collectors.toList());
        log.info("Player ids going={}", going);
        return going;
    }

    public Optional<String> getLastPollId(Long chatId) {
        Optional<PollEntity> poll = pollRepository.findFirstByChatIdOrderByCreationTimestampDesc(chatId);
        return poll.map(PollEntity::getId);
    }

    public Optional<Integer> getLastPollMessageId(Long chatId) {
        Optional<PollEntity> poll = pollRepository.findFirstByChatIdOrderByCreationTimestampDesc(chatId);
        return poll.map(PollEntity::getMessageId);
    }
}
