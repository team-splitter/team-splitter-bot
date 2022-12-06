package com.max.team.splitter.strategy;

import com.max.team.splitter.model.Player;
import com.max.team.splitter.model.PlayerScore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "BackAndForceTeamSplitStrategy")
public class BackAndForceTeamSplitStrategy implements TeamSplitStrategy{

    public List<List<Player>> split(int numberOfTeams, List<PlayerScore> playerScores) {
        List<List<Player>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int i = 0;
        int direction = -1;
        for (PlayerScore playerScore : playerScores) {
            if (i % numberOfTeams == 0) {
                direction = direction == -1 ? 0 : -1;
            }
            int teamIndex = direction == 0 ? i % numberOfTeams : numberOfTeams - 1 - (i%numberOfTeams);
            teams.get(teamIndex).add(playerScore.getPlayer());
            i++;
        }
        return teams;
    }


}
