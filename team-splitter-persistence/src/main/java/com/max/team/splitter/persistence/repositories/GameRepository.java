package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.GameEntity;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> findByGameSplitId(Long gameSplitId);

    void deleteByGameSplitId(Long gameSplitId);
}
