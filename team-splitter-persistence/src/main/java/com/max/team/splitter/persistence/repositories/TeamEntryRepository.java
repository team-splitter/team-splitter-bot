package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamEntryRepository extends JpaRepository<TeamEntryEntity, Long> {

    List<TeamEntryEntity> findByGameIdIn(List<Long> gameIds);

    List<TeamEntryEntity> findByGameIdAndTeamName(Long gameId, String teamName);

    void deleteByGameIdAndPlayerId(Long gameId, Long playerId);
    void deleteByGameId(Long gameId);
}
