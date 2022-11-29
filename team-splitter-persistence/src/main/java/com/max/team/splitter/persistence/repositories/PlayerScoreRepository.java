package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.PlayerScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerScoreRepository extends JpaRepository<PlayerScoreEntity, Long> {
}
