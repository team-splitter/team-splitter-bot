package com.max.team.splitter.core.strategy;

import com.max.team.splitter.core.TestData;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BackAndForceTeamSplitStrategyTest {

    BackAndForceTeamSplitStrategy splitStrategy = new BackAndForceTeamSplitStrategy();

    @Test
    void splitIntoTwoTeams() {
        PlayerEntity p1 = TestData.createPlayer(1L, 80);
        PlayerEntity p2 = TestData.createPlayer(2L, 75);
        PlayerEntity p3 = TestData.createPlayer(3L, 75);
        PlayerEntity p4 = TestData.createPlayer(4L, 70);
        PlayerEntity p5 = TestData.createPlayer(5L, 65);
        PlayerEntity p6 = TestData.createPlayer(6L, 65);
        PlayerEntity p7 = TestData.createPlayer(7L, 60);
        PlayerEntity p8 = TestData.createPlayer(8L, 55);
        List<List<PlayerEntity>> teams = splitStrategy.split(2, List.of(
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
        PlayerEntity p1 = TestData.createPlayer(1L, 80);
        PlayerEntity p2 = TestData.createPlayer(2L, 75);
        PlayerEntity p3 = TestData.createPlayer(3L, 75);
        PlayerEntity p4 = TestData.createPlayer(4L, 70);
        PlayerEntity p5 = TestData.createPlayer(5L, 65);
        PlayerEntity p6 = TestData.createPlayer(6L, 65);
        PlayerEntity p7 = TestData.createPlayer(7L, 60);
        PlayerEntity p8 = TestData.createPlayer(8L, 55);
        PlayerEntity p9 = TestData.createPlayer(9L, 55);
        List<List<PlayerEntity>> teams = splitStrategy.split(3, List.of(
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
        PlayerEntity p1 = TestData.createPlayer(1L, 80);
        PlayerEntity p2 = TestData.createPlayer(2L, 75);
        PlayerEntity p3 = TestData.createPlayer(3L, 75);
        PlayerEntity p4 = TestData.createPlayer(4L, 70);
        PlayerEntity p5 = TestData.createPlayer(5L, 65);
        PlayerEntity p6 = TestData.createPlayer(6L, 65);
        PlayerEntity p7 = TestData.createPlayer(7L, 60);
        PlayerEntity p8 = TestData.createPlayer(8L, 55);
        PlayerEntity p9 = TestData.createPlayer(9L, 55);
        PlayerEntity p10 = TestData.createPlayer(10L, 50);
        PlayerEntity p11 = TestData.createPlayer(11L, 50);
        PlayerEntity p12 = TestData.createPlayer(12L, 45);
        List<List<PlayerEntity>> teams = splitStrategy.split(4, List.of(
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