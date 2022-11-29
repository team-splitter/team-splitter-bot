package com.max.team.splitter.service;

import com.max.team.splitter.model.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class HighestScoreTeamSplitterServiceTest {
    private ScoresService scoresService = mock(ScoresService.class);
    private HighestScoreTeamSplitterService service = new HighestScoreTeamSplitterService(scoresService);

    private static Map<Long, Integer> scores = new HashMap<>();

    static  {
        scores.put(1L, 65);
        scores.put(2L, 30);
        scores.put(3L, 55);
        scores.put(4L, 45);
        scores.put(5L, 70);
        scores.put(6L, 80);
    }
    @Test
    public void splitIntoTwoTeams() {

        when(scoresService.getScores()).thenReturn(scores);

        Player p1 = createPlayer(1L);
        Player p2 = createPlayer(2L);
        Player p3 = createPlayer(3L);
        Player p4 = createPlayer(4L);
        Player p5 = createPlayer(5L);
        Player p6 = createPlayer(6L);
        Player p7 = createPlayer(7L);
        Player p8 = createPlayer(8L);
        List<List<Player>> teams = service.splitIntoTeams(2, List.of(
                p1,
                p2,
                p3,
                p4,
                p5,
                p6,
                p7,
                p8
        ));

        assertThat(teams).containsExactly(
                List.of(p6, p1, p7, p4),
                List.of(p5, p3, p8, p2)
        );
    }

    private Player createPlayer(Long id) {
        Player player = new Player(id);
        return player;
    }
}