package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.Player;
import com.max.team.splitter.core.model.PollAnswerModel;
import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.service.PlayerService;
import com.max.team.splitter.core.service.PollService;
import com.max.team.splitter.server.dto.PollVote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/poll")
public class PollController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PollService pollService;
    @Autowired
    private PlayerService playerService;


    @RequestMapping(method = RequestMethod.GET)
    public List<PollModel> listPolls() {
        log.info("Getting list of polls");
        List<PollModel> all = pollService.getAll();
        return all;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PollModel getById(@PathVariable(name = "id") String pollId) {
        log.info("Getting poll by id={}", pollId);

        return pollService.getById(pollId);
    }

    @RequestMapping(value = "/{id}/vote", method = RequestMethod.POST)
    public PollVote addPlayerVoteToPoll(@PathVariable(value = "id") String pollId, @RequestBody PollVote body) {
        PollAnswerModel answer = pollService.addPollAnswer(pollId, body.getPlayer().getId());

        PollVote vote = new PollVote();
        vote.setId(answer.getId());
        vote.setPlayer(playerService.getPlayer(answer.getPlayerId()));
        return vote;
    }

    @RequestMapping(value = "/{id}/vote/{voteId}", method = RequestMethod.DELETE)
    public boolean deleteVote(@PathVariable(value = "id") String pollId, @PathVariable("voteId") Long voteId) {
        log.info("Delete vote for poll={} and voteId={}", pollId, voteId);
        pollService.deletePollAnswer(pollId, voteId);

        return true;
    }

    @RequestMapping(value = "/{id}/vote", method = RequestMethod.GET)
    public List<PollVote> getVotes(@PathVariable("id") String pollId) {
        Map<Long, Player> map =  playerService.getPlayers().stream().collect(Collectors.toMap(Player::getId, Function.identity()));
        List<PollAnswerModel> answers = pollService.getVotesForPoll(pollId);

        return answers.stream().map((answer) -> {
            PollVote vote = new PollVote();
            vote.setId(answer.getId());
            vote.setPlayer(map.get(answer.getPlayerId()));
            return vote;
        }).collect(Collectors.toList());
    }

}
