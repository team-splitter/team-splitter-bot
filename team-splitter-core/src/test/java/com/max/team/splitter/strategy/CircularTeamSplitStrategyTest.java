package com.max.team.splitter.strategy;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.model.PlayerScore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.max.team.splitter.TestData.createPlayer;
import static org.assertj.core.api.Assertions.assertThat;


class CircularTeamSplitStrategyTest {
    CircularTeamSplitStrategy splitStrategy = new CircularTeamSplitStrategy();

    @Test
    void splitIntoTwoTeams() {
        Player p1 = createPlayer(1L);
        Player p2 = createPlayer(2L);
        Player p3 = createPlayer(3L);
        Player p4 = createPlayer(4L);
        Player p5 = createPlayer(5L);
        Player p6 = createPlayer(6L);
        Player p7 = createPlayer(7L);
        Player p8 = createPlayer(8L);
        List<List<Player>> teams = splitStrategy.split(2, List.of(
                new PlayerScore(p1, 80),
                new PlayerScore(p2, 75),
                new PlayerScore(p3, 75),
                new PlayerScore(p4, 70),
                new PlayerScore(p5, 65),
                new PlayerScore(p6, 65),
                new PlayerScore(p7, 60),
                new PlayerScore(p8, 55)
        ));

        assertThat(teams).containsExactly(
                List.of(p1, p3, p5, p7),
                List.of(p2, p4, p6, p8)
        );
    }

    @Test
    void splitIntoThreeTeams() {
        Player p1 = createPlayer(1L);
        Player p2 = createPlayer(2L);
        Player p3 = createPlayer(3L);
        Player p4 = createPlayer(4L);
        Player p5 = createPlayer(5L);
        Player p6 = createPlayer(6L);
        Player p7 = createPlayer(7L);
        Player p8 = createPlayer(8L);
        Player p9 = createPlayer(9L);
        List<List<Player>> teams = splitStrategy.split(3, List.of(
                new PlayerScore(p1, 80),
                new PlayerScore(p2, 75),
                new PlayerScore(p3, 75),
                new PlayerScore(p4, 70),
                new PlayerScore(p5, 65),
                new PlayerScore(p6, 65),
                new PlayerScore(p7, 60),
                new PlayerScore(p8, 55),
                new PlayerScore(p9, 55)
        ));

        assertThat(teams).containsExactly(
                List.of(p1, p4, p7),
                List.of(p2, p5, p8),
                List.of(p3, p6, p9)
        );
    }


}