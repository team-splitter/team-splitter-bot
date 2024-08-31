package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Team;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.core.service.TeamSplitterService;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.max.team.splitter.core.Constants.DEFAULT_SCORE;

@RestController
@RequestMapping("/team")
public class TeamSplitterController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TeamSplitterService teamSplitterService;
    @Autowired
    private PollService pollService;
    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/split/{pollId}", method = RequestMethod.GET)
    public List<Team>  split(@PathVariable(value = "pollId") String pollId,
                                @RequestParam(name = "teamsNum", defaultValue = "2") Integer numberOfTeams,
                                @RequestParam(name = "splitType", defaultValue = "TEAM_SCORE_BALANCE", required = false) SplitterStrategyType splitType) {
        if (numberOfTeams < 2 || numberOfTeams > 4) {
            //set default as 2
            numberOfTeams = 2;
        }
        log.info("Splitting teams for pollId={} into {} teams", pollId, numberOfTeams);

        List<Long> playerIds = pollService.getPlayerIdsGoingToGame(pollId);
        List<PlayerEntity> players = playerService.getPlayersByIds(playerIds);
        players.forEach(player -> player.setScore(player.getScore() != null ? player.getScore() : DEFAULT_SCORE));

        Map<String, List<PlayerEntity>> teamsMap = teamSplitterService.splitTeams(numberOfTeams, splitType, players);

        List<Team> teams = new LinkedList<>();
        for (Map.Entry<String, List<PlayerEntity>> entry : teamsMap.entrySet()) {
            Team team = new Team();
            team.setName(entry.getKey());
            team.setPlayers(entry.getValue());
            teams.add(team);
        }

        return teams;
    }

}
