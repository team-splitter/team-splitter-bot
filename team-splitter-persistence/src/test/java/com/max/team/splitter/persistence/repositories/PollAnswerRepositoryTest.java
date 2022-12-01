package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.PersistenceConfig;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
@SpringBootTest(properties = { "db=h2" })
public class PollAnswerRepositoryTest {

    @Autowired
    private PollAnswerRepository pollAnswerRepository;

    @Test
    void test_create() {
        String pollId = "xxx";
        Long playerId = 5L;
        PollAnswerEntity entity = new PollAnswerEntity();
        entity.setId(1L);
        entity.setPollId(pollId);
        entity.setPlayerId(playerId);
        entity.setCreationTimestamp(Instant.now());
        pollAnswerRepository.save(entity);

        Optional<PollAnswerEntity> player = pollAnswerRepository.findById(1L);

        assertThat(player).isNotEmpty();

        Optional<PollAnswerEntity> existing = pollAnswerRepository.findByPollIdAndPlayerId(pollId, playerId);

        pollAnswerRepository.delete(existing.orElseThrow(() -> new IllegalStateException("")));
        player = pollAnswerRepository.findById(1L);
        assertThat(player).isEmpty();
    }
}