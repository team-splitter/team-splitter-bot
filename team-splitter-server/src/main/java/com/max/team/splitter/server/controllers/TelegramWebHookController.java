package com.max.team.splitter.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/webhook/telegram")
public class TelegramWebHookController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.POST)
    public Response handle(@RequestBody LinkedHashMap body) {
        log.info("Handle request={}", body);
        return new Response("SUCCESS");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Response handleGet() {
        log.info("Handle Get request");
        return new Response("SUCCESS");

    }

    static class Response {
        private String status;

        public Response(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
