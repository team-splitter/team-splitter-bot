package com.max.team.splitter.persistence.repositories;

import com.max.team.splitter.persistence.entities.GameSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameScheduleRepository extends JpaRepository<GameSchedule, Long> {

}
