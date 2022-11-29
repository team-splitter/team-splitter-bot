package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.PersistenceConfig;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.PlayerScoreEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
@SpringBootTest(properties = { "db=h2" })
public class PlayerScoreRepositoryTest {

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Test
    void test_create() {
        PlayerScoreEntity entity = new PlayerScoreEntity();
        entity.setPlayerId(1L);
        entity.setScore(60);
        playerScoreRepository.save(entity);

        Optional<PlayerScoreEntity> player = playerScoreRepository.findById(1L);

        assertThat(player).isNotEmpty();
    }
}