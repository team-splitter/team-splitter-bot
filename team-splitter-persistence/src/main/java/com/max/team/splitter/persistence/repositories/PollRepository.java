package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.PollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<PollEntity, String> {

    Optional<PollEntity> findFirstByChatIdOrderByCreationTimestampDesc(Long chatId);
}
