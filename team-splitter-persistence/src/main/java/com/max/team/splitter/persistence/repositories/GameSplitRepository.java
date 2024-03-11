package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.GameSplitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSplitRepository extends JpaRepository<GameSplitEntity, Long> {
    List<GameSplitEntity> findByPollId(String pollId);
}
