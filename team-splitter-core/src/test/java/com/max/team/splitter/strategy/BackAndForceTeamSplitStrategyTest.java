package com.max.team.splitter.strategy;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.model.PlayerScore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.max.team.splitter.TestData.createPlayer;
import static org.assertj.core.api.Assertions.assertThat;

class BackAndForceTeamSplitStrategyTest {

    BackAndForceTeamSplitStrategy splitStrategy = new BackAndForceTeamSplitStrategy();

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
                List.of(p1, p4, p5, p8),
                List.of(p2, p3, p6, p7)
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
                List.of(p1, p6, p7),
                List.of(p2, p5, p8),
                List.of(p3, p4, p9)
        );
    }

    @Test
    void splitIntoFourTeams() {
        Player p1 = createPlayer(1L);
        Player p2 = createPlayer(2L);
        Player p3 = createPlayer(3L);
        Player p4 = createPlayer(4L);
        Player p5 = createPlayer(5L);
        Player p6 = createPlayer(6L);
        Player p7 = createPlayer(7L);
        Player p8 = createPlayer(8L);
        Player p9 = createPlayer(9L);
        Player p10 = createPlayer(10L);
        Player p11 = createPlayer(11L);
        Player p12 = createPlayer(12L);
        List<List<Player>> teams = splitStrategy.split(4, List.of(
                new PlayerScore(p1, 80),
                new PlayerScore(p2, 75),
                new PlayerScore(p3, 75),
                new PlayerScore(p4, 70),
                new PlayerScore(p5, 65),
                new PlayerScore(p6, 65),
                new PlayerScore(p7, 60),
                new PlayerScore(p8, 55),
                new PlayerScore(p9, 55),
                new PlayerScore(p10, 50),
                new PlayerScore(p11, 50),
                new PlayerScore(p12, 45)
        ));

        assertThat(teams).containsExactly(
                List.of(p1, p8, p9),
                List.of(p2, p7, p10),
                List.of(p3, p6, p11),
                List.of(p4, p5, p12)
        );
    }
}