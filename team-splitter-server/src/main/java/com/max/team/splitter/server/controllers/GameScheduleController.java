package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.service.GameScheduleService;
import com.max.team.splitter.persistence.entities.GameSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game-schedule")
public class GameScheduleController {
    @Autowired
    private GameScheduleService gameScheduleService;

    @RequestMapping(method = RequestMethod.GET)
    public List<GameSchedule> getAllSchedules() {
        return gameScheduleService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public GameSchedule getById(@PathVariable(name = "id") Long id) {
        return gameScheduleService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public GameSchedule create(@RequestBody GameSchedule body) {
        return gameScheduleService.create(body);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable("id") Long id) {
        gameScheduleService.delete(id);
        return true;
    }
}
