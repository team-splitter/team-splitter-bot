package com.max.team.splitter.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScoresService {
    private static Map<Long, Integer> scores = new HashMap<>();

    static  {
        scores.put(1L, 65);
        scores.put(2L, 30);
        scores.put(3L, 55);
        scores.put(4L, 45);
        scores.put(5L, 70);
        scores.put(6L, 80);
    }

    public Map<Long, Integer> getScores() {
        return Collections.unmodifiableMap(scores);
    }
}
