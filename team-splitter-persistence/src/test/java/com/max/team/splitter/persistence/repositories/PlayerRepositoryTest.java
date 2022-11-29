package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.PersistenceConfig;
import com.max.team.splitter.persistence.entities.PlayerEntity;
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
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void test_create() {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(1L);
        entity.setFirstName("Test");
        playerRepository.save(entity);

        Optional<PlayerEntity> player = playerRepository.findById(1L);

        assertThat(player).isNotEmpty();
    }
}