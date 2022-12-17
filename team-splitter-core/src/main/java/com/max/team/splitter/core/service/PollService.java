package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.exception.NotFoundException;
import com.max.team.splitter.core.model.PollAnswerModel;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import com.max.team.splitter.persistence.entities.PollEntity;
import com.max.team.splitter.persistence.repositories.PollAnswerRepository;
import com.max.team.splitter.persistence.repositories.PollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PollService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PollRepository pollRepository;
    private final PollAnswerRepository pollAnswerRepository;

    public PollService(PollRepository pollRepository, PollAnswerRepository pollAnswerRepository) {
        this.pollRepository = pollRepository;
        this.pollAnswerRepository = pollAnswerRepository;
    }

    public List<PollModel> getAll() {
        List<PollEntity> all = pollRepository.findAll();
        return all.stream().map(CoreConverters::toPollModel).collect(Collectors.toList());
    }

    public PollModel getById(String pollId) {
        PollEntity entity = pollRepository.findById(pollId).orElseThrow(() -> new NotFoundException("Not found by pollId=" + pollId));

        return CoreConverters.toPollModel (entity);
    }

    public void addPoll(PollModel pollModel) {
        PollEntity entity = CoreConverters.toPollEntity(pollModel);
        pollRepository.save(entity);
    }


    public void addPollAnswer(PollAnswerModel pollAnswer) {
        Integer[] optionIds = pollAnswer.getOptionIds();
        String pollId = pollAnswer.getPollId();
        Long playerId = pollAnswer.getPlayerId();

        if (optionIds.length == 0) {
            //retract vote
            log.info("retract vote, removing player by id={}", playerId);
            Optional<PollAnswerEntity> existing = pollAnswerRepository.findByPollIdAndPlayerId(pollId, playerId);
            if (existing.isPresent()) {
                log.info("Deleting existing poll answer with id={}", existing.get().getPollId());
                pollAnswerRepository.delete(existing.get());
            }
        } else if (optionIds[0] == 0) {
            //vote +
            log.info("adding player with id={}", playerId);
            PollAnswerEntity entity = CoreConverters.toPollAnswerEntity(pollAnswer);
            pollAnswerRepository.save(entity);
        }
    }

    public PollAnswerModel addPollAnswer(String pollId, Long playerId) {
        Optional<PollAnswerEntity> found = pollAnswerRepository.findByPollIdAndPlayerId(pollId, playerId);
        if (found.isPresent()) {
            return CoreConverters.toPollAnswerModel(found.get());
        }
        PollAnswerEntity entity = CoreConverters.toPollAnswerEntity(pollId, playerId);
        PollAnswerEntity vote = pollAnswerRepository.save(entity);
        return CoreConverters.toPollAnswerModel(vote);
    }

    public void deletePollAnswer(String pollId, Long playerId) {
        PollAnswerEntity entity = pollAnswerRepository.findByPollIdAndPlayerId(pollId, playerId)
                .orElseThrow(() -> new NotFoundException("Not fount by pollId=" + pollId + " and playerId=" + playerId));
        pollAnswerRepository.delete(entity);
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

    public List<PollAnswerModel> getVotesForPoll(String pollId) {
        List<PollAnswerEntity> answers = pollAnswerRepository.findByPollId(pollId);

        return answers.stream().map(CoreConverters::toPollAnswerModel).collect(Collectors.toList());
    }
}
