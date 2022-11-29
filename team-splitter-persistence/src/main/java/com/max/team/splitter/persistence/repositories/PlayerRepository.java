package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    List<PlayerEntity> findByIdIn(List<Long> ids);
}
