package com.max.team.splitter.core.service;

import com.max.team.splitter.core.exception.NotFoundException;
import com.max.team.splitter.persistence.entities.GameSchedule;
import com.max.team.splitter.persistence.entities.GameScheduleStatus;
import com.max.team.splitter.persistence.repositories.GameScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameScheduleService {
    @Autowired
    private GameScheduleRepository gameScheduleRepository;

    public List<GameSchedule> getAll() {
        return gameScheduleRepository.findAll();
    }

    public GameSchedule create(GameSchedule schedule) {
        GameSchedule saved = gameScheduleRepository.save(schedule);
        return saved;
    }

    public void delete(Long id) {
        gameScheduleRepository.deleteById(id);
    }

    public GameSchedule get(Long id) {
        return gameScheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Game schedule not found by id=" + id));
    }

    public void updateStatus(Long id, GameScheduleStatus newStatus) {
        GameSchedule gameSchedule = gameScheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Poll not found by id=" + id));
        gameSchedule.setStatus(newStatus);
        gameScheduleRepository.save(gameSchedule);
    }

    public void update(GameSchedule gameSchedule) {
        gameScheduleRepository.save(gameSchedule);
    }
}
