package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.core.service.ScoresService;
import com.max.team.splitter.core.service.TeamSplitterService;
import com.max.team.splitter.server.dto.PlayerDto;
import com.max.team.splitter.server.dto.TeamDto;
import liquibase.pro.packaged.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.max.team.splitter.server.converters.DtoConverters.toPlayerDto;

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
    @Autowired
    private ScoresService scoresService;

    @RequestMapping(value = "/split/{pollId}", method = RequestMethod.GET)
    public List<TeamDto>  split(@PathVariable(value = "pollId") String pollId,
                                @RequestParam(name = "teamsNum", defaultValue = "2") Integer numberOfTeams) {
        if (numberOfTeams < 2 || numberOfTeams > 4) {
            //set default as 2
            numberOfTeams = 2;
        }
        log.info("Splitting teams for pollId={} into {} teams", pollId, numberOfTeams);

        List<Long> playerIds = pollService.getPlayerIdsGoingToGame(pollId);
        List<Player> players = playerService.getPlayersByIds(playerIds);
        Map<String, List<Player>> teamsMap = teamSplitterService.splitTeams(numberOfTeams, players);
        Map<Long, Integer> playerScores = scoresService.getScores();

        List<TeamDto> teams = new LinkedList<>();
        for (Map.Entry<String, List<Player>> entry : teamsMap.entrySet()) {
            TeamDto team = new TeamDto();
            team.setName(entry.getKey());
            team.setPlayers(toPlayerDtoList(entry.getValue(), playerScores));
            teams.add(team);
        }

        return teams;
    }

    private List<PlayerDto> toPlayerDtoList(List<Player> players, Map<Long, Integer> playerScores) {
        List<PlayerDto> playerDtos = new LinkedList<>();
        for (Player player : players) {
            PlayerDto dto = toPlayerDto(player);
            dto.setScore(playerScores.get(player.getId()));

            playerDtos.add(dto);
        }

        return playerDtos;
    }
}
