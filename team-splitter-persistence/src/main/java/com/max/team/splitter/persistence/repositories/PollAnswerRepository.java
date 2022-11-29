package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.PollAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollAnswerRepository extends JpaRepository<PollAnswerEntity, Long> {

    List<PollAnswerEntity> findByPollId(String pollId);
}
