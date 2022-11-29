package com.max.team.splitter.service;

import com.max.team.splitter.model.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HighestScoreTeamSplitterService implements TeamSplitterService {
    private static final int DEFAULT_SCORE = 50;
    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;

    private final ScoresService scoresService;

    public HighestScoreTeamSplitterService(ScoresService scoresService) {
        this.scoresService = scoresService;
    }

    @Override
    public List<List<Player>> splitIntoTeams(int numberOfTeams, List<Player> players) {
        List<PlayerScore> playerScores = getPlayerScores(players);

        //Sort by score descending then by player id
        playerScores.sort((a, b) -> {
            if (a.score > b.score) {
                return -1;
            } else if (a.score < b.score) {
                return 1;
            } else {
                return a.player.getId().compareTo(b.player.getId());
            }
        });
        List<List<Player>> teams = new ArrayList<>();
        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new ArrayList<>());
        }

        int i = 0;
        for (PlayerScore playerScore : playerScores) {
            teams.get(i % numberOfTeams).add(playerScore.player);
            i++;
        }
        return teams;
    }

    private List<PlayerScore> getPlayerScores(List<Player> players) {
        Map<Long, Integer> scores = scoresService.getScores();
        return players.stream()
                .map((player -> new PlayerScore(player, scores.getOrDefault(player.getId(), DEFAULT_SCORE))))
                .collect(Collectors.toList());
    }

    static class PlayerScore {
        Player player;
        int score;

        public PlayerScore(Player player, int score) {
            this.player = player;
            this.score = score;
        }
    }
}
