package com.max.team.splitter.server.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.team.splitter.core.bot.handlers.CompositeUpdateHandler;
import com.max.team.splitter.core.bot.service.TelegramBotService;
import com.max.team.splitter.core.model.telegram.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/telegram")
public class TelegramWebHookController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CompositeUpdateHandler updateHandler;


    public TelegramWebHookController(CompositeUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handle(@RequestBody String request) {
        log.info("Handle request={}", request);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

        try {
            Update update = mapper.readValue(request, Update.class);
            log.info("Update={}", update);
            updateHandler.handle(update);
        } catch (Exception e) {
            log.error("Failed to handle telegram update", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
