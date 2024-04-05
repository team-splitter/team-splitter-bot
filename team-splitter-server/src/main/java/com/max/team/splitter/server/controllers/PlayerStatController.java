package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.PlayerStat;
import com.max.team.splitter.core.service.PlayerStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping(value = "/player_stat")
public class PlayerStatController {
    private static final Logger log = LoggerFactory.getLogger(PlayerStatController.class);
    private final PlayerStatService playerStatService;

    public PlayerStatController(PlayerStatService playerStatService) {
        this.playerStatService = playerStatService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PlayerStat> getPlayerStats(@RequestParam(value = "startDate", required = false) String startDateStr, @RequestParam(value = "endDate", required = false) String endDateStr) {
        log.info("Getting player stats startDate={}, endDate={}", startDateStr, endDateStr);
        LocalDate startDate;
        LocalDate endDate;
        LocalDate now = LocalDate.now();
        if (endDateStr == null) {
            endDate = now;
        } else {
            endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (startDateStr == null) {
            startDate = now.minus(30, ChronoUnit.DAYS);
        } else {
            startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return playerStatService.getPlayerStats(startDate, endDate);
    }
}
