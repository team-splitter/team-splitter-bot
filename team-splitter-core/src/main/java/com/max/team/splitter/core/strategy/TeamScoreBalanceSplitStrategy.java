package com.max.team.splitter.core.strategy;

import com.max.team.splitter.core.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Component(value = "TeamScoreBalanceSplitStrategy")
public class TeamScoreBalanceSplitStrategy implements TeamSplitStrategy {

    @Override
    public List<List<Player>> split(int numberOfTeams, List<Player> players) {
        PriorityQueue<Object[]> q = new PriorityQueue<>((o1, o2) -> {
            int teamScore1 = (int) o1[0];
            int teamScore2 = (int) o2[0];
            return teamScore1 - teamScore2;
        });

        List<List<Player>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
            q.add(new Object[]{0, teams.get(i)});
        }


        int i = 0;
        for (Player player : players) {
            Object[] team = q.poll();
            int teamScore = (int) team[0];
            List<Player> teamPlayers = (List<Player>) team[1];
            teamScore += player.getScore();
            teamPlayers.add(player);

            q.add(new Object [] {teamScore, teamPlayers});

            i++;
        }
        return teams;
    }

    @Override
    public SplitterStrategyType getType() {
        return SplitterStrategyType.TEAM_SCORE_BALANCE;
    }
}
