package com.max.team.splitter.core.strategy;

import com.max.team.splitter.core.TestData;
import com.max.team.splitter.core.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BackAndForceTeamSplitStrategyTest {

    BackAndForceTeamSplitStrategy splitStrategy = new BackAndForceTeamSplitStrategy();

    @Test
    void splitIntoTwoTeams() {
        Player p1 = TestData.createPlayer(1L, 80);
        Player p2 = TestData.createPlayer(2L, 75);
        Player p3 = TestData.createPlayer(3L, 75);
        Player p4 = TestData.createPlayer(4L, 70);
        Player p5 = TestData.createPlayer(5L, 65);
        Player p6 = TestData.createPlayer(6L, 65);
        Player p7 = TestData.createPlayer(7L, 60);
        Player p8 = TestData.createPlayer(8L, 55);
        List<List<Player>> teams = splitStrategy.split(2, List.of(
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
                List.of(p1, p4, p5, p8),
                List.of(p2, p3, p6, p7)
        );
    }

    @Test
    void splitIntoThreeTeams() {
        Player p1 = TestData.createPlayer(1L, 80);
        Player p2 = TestData.createPlayer(2L, 75);
        Player p3 = TestData.createPlayer(3L, 75);
        Player p4 = TestData.createPlayer(4L, 70);
        Player p5 = TestData.createPlayer(5L, 65);
        Player p6 = TestData.createPlayer(6L, 65);
        Player p7 = TestData.createPlayer(7L, 60);
        Player p8 = TestData.createPlayer(8L, 55);
        Player p9 = TestData.createPlayer(9L, 55);
        List<List<Player>> teams = splitStrategy.split(3, List.of(
                p1,
                p2,
                p3,
                p4,
                p5,
                p6,
                p7,
                p8,
                p9
        ));

        assertThat(teams).containsExactly(
                List.of(p1, p6, p7),
                List.of(p2, p5, p8),
                List.of(p3, p4, p9)
        );
    }

    @Test
    void splitIntoFourTeams() {
        Player p1 = TestData.createPlayer(1L, 80);
        Player p2 = TestData.createPlayer(2L, 75);
        Player p3 = TestData.createPlayer(3L, 75);
        Player p4 = TestData.createPlayer(4L, 70);
        Player p5 = TestData.createPlayer(5L, 65);
        Player p6 = TestData.createPlayer(6L, 65);
        Player p7 = TestData.createPlayer(7L, 60);
        Player p8 = TestData.createPlayer(8L, 55);
        Player p9 = TestData.createPlayer(9L, 55);
        Player p10 = TestData.createPlayer(10L, 50);
        Player p11 = TestData.createPlayer(11L, 50);
        Player p12 = TestData.createPlayer(12L, 45);
        List<List<Player>> teams = splitStrategy.split(4, List.of(
                p1,
                p2,
                p3,
                p4,
                p5,
                p6,
                p7,
                p8,
                p9,
                p10,
                p11,
                p12
        ));

        assertThat(teams).containsExactly(
                List.of(p1, p8, p9),
                List.of(p2, p7, p10),
                List.of(p3, p6, p11),
                List.of(p4, p5, p12)
        );
    }
}