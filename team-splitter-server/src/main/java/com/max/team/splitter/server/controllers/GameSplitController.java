package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.GameScore;
import com.max.team.splitter.core.model.GameSplit;
import com.max.team.splitter.core.service.GameSplitService;
import com.max.team.splitter.core.strategy.SplitterStrategyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/game_split")
public class GameSplitController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final GameSplitService gameSplitService;

    public GameSplitController(GameSplitService gameSplitService) {
        this.gameSplitService = gameSplitService;
    }


    @RequestMapping(value = "/poll/{pollId}", method = RequestMethod.GET)
    public List<GameSplit> gameSplitsByPollId(@PathVariable(name = "pollId") String pollId) {
        log.info("Getting list of game splits by pollId={}", pollId);
        List<GameSplit> splits = gameSplitService.getGameSplitsByPollId(pollId);
        splits.sort(Comparator.comparing(GameSplit::getCreationTime).reversed());

        return splits;
    }

    @RequestMapping(value = "/split", method = RequestMethod.POST)
    public GameSplit slit(@RequestParam(name = "pollId") String pollId,
                                @RequestParam(name = "numberOfTeams") Integer numberOfTeams,
                                @RequestParam(name = "splitAlg")SplitterStrategyType splitAlg) {
        log.info("Split  pollId={}, numberOfTeams={}, splitAlg={}", pollId, numberOfTeams, splitAlg);
        GameSplit split = gameSplitService.split(pollId, numberOfTeams, splitAlg);

        return split;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GameSplit> getAllGameSplits() {
        List<GameSplit> splits = gameSplitService.getAllGameSplits();
        splits.sort(Comparator.comparing(GameSplit::getCreationTime).reversed());
        return splits;
    }


    @RequestMapping(value = "/{gameSplitId}/team_entry/{playerId}", method = RequestMethod.DELETE)
    public void deleteTeamEntry(@PathVariable("gameSplitId") Long gameSplitId,
                                @PathVariable("playerId") Long playerId) {
        log.info("Delete team entry gameSplitId={}, playerId={}", gameSplitId, playerId);
        gameSplitService.deleteTeamEntry(gameSplitId, playerId);
    }

    @RequestMapping(value = "/{gameSplitId}", method = RequestMethod.DELETE)
    public void deleteGameSplit(@PathVariable("gameSplitId") Long gameSplitId) {
        log.info("Delete game split by gameSplitId={}", gameSplitId);
        gameSplitService.deleteGameSplit(gameSplitId);
    }

    @RequestMapping(value = "/{gameSplitId}/score", method = RequestMethod.POST)
    public GameSplit setGameSplitScores(@PathVariable("gameSplitId") Long gameSplitId, @RequestBody List<GameScore> scores) {
        return gameSplitService.setScores(gameSplitId, scores);
    }


}
