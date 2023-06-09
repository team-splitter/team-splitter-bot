package com.max.team.splitter.core.service;

import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.model.PlayerStat;
import com.max.team.splitter.persistence.entities.PlayerStatEntity;
import com.max.team.splitter.persistence.repositories.PlayerStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerStatService {
    private final PlayerStatRepository playerStatRepository;

    public PlayerStatService(PlayerStatRepository playerStatRepository) {
        this.playerStatRepository = playerStatRepository;
    }

    public List<PlayerStat> getPlayerStats() {
        return playerStatRepository.findAll().stream().map(CoreConverters::toPlayerStat).collect(Collectors.toList());
    }

    public PlayerStat getPlayerStat(Long playerId) {
        return CoreConverters.toPlayerStat(playerStatRepository.getOne(playerId));
    }
}
