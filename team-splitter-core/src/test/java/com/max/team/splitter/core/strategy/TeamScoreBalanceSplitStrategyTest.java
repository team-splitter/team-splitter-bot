package com.max.team.splitter.core.strategy;

import com.max.team.splitter.persistence.entities.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.max.team.splitter.core.TestData.playersList;

class TeamScoreBalanceSplitStrategyTest {

    private TeamScoreBalanceSplitStrategy splitStrategy = new TeamScoreBalanceSplitStrategy();

//    @Test
    void splitIntoTwoTeams() {

        List<List<PlayerEntity>> teams = splitStrategy.split(4, playersList());

        printTeams(teams);

    }

    private void printTeams(List<List<PlayerEntity>> teams) {
        for (List<PlayerEntity> team : teams) {
            int sum = 0;
            for (PlayerEntity player : team) {
                System.out.println(player.getScore() + ", id:" + player.getId() + ", " + player.getFirstName()  + " " + player.getLastName());
                sum += player.getScore();
            }

            System.out.println("Team score: " + sum);
        }
    }
}