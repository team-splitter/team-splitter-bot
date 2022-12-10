package com.max.team.splitter.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private static Logger logger = LoggerFactory.getLogger(RootController.class);

    @RequestMapping("/")
    public String root() {
        logger.info("Root path called");
        return "Team Splitter Service";
    }
}
