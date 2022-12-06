package com.max.team.splitter.strategy;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.model.PlayerScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "CircularTeamSplitStrategy")
public class CircularTeamSplitStrategy implements TeamSplitStrategy{
    @Override
    public List<List<Player>> split(int numberOfTeams, List<PlayerScore> playerScores) {
        List<List<Player>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int i = 0;
        for (PlayerScore playerScore : playerScores) {
            teams.get(i % numberOfTeams).add(playerScore.getPlayer());
            i++;
        }
        return teams;
    }

}
