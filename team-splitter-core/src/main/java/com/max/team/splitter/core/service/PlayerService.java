package com.max.team.splitter.core.service;

import com.max.team.splitter.core.exception.NotFoundException;
import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerEntity> getPlayers() {
        return playerRepository.findAll();
    }
    public PlayerEntity getPlayer(Long id) {
        Optional<PlayerEntity> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new NotFoundException();
        }
    }

    public boolean createPlayer(PlayerEntity player) {
        Long id = player.getId();
        log.info("Creating player with id={}", id);
        boolean exists= playerRepository.existsById(id);
        if (exists) {
            log.info("PlayerEntity with id={} already exists", id);
            return false;
        } else {
            PlayerEntity savedEntity = playerRepository.save(player);
            log.info("PlayerEntity with id={} has added", id);
            return true;
        }
    }

    public PlayerEntity updatePlayer(Long playerId, PlayerEntity player) {
        log.info("Updating player with id={}", playerId);
        Optional<PlayerEntity> getPlayer = playerRepository.findById(playerId);
        PlayerEntity existingPlayer = getPlayer
                .orElseThrow(() -> new NotFoundException("PlayerEntity with id=" + playerId + " is not found"));

        existingPlayer.setFirstName(player.getFirstName());
        existingPlayer.setLastName(player.getLastName());
        existingPlayer.setScore(player.getScore());

        return playerRepository.save(existingPlayer);
    }

    public List<PlayerEntity> getPlayersByIds(Collection<Long> ids) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(ids);

        return entities;
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public void updatePlayerScores(Map<Long, Integer> playerPoints) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(playerPoints.keySet());
        entities.forEach((entity -> {entity.setScore(entity.getScore() + playerPoints.get(entity.getId()));}));

        playerRepository.saveAll(entities);
    }
}
