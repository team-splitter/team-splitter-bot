package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.PlayerStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatRepository extends JpaRepository<PlayerStatEntity, Long> {

}
