package com.max.team.splitter.server.controllers;

import com.max.team.splitter.core.model.PollModel;
import com.max.team.splitter.core.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/poll")
public class PollController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PollService pollService;


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

}
