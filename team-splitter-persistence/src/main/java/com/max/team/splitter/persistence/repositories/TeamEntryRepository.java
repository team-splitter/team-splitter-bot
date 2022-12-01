package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.TeamEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamEntryRepository extends JpaRepository<TeamEntryEntity, Long> {
}
