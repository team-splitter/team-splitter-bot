package com.max.team.splitter.core.service;

import com.max.team.splitter.core.exception.NotFoundException;
import com.max.team.splitter.core.converter.CoreConverters;
import com.max.team.splitter.core.model.Player;
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

    public List<Player> getPlayers() {
        return playerRepository.findAll().stream().map(CoreConverters::toPlayer).collect(Collectors.toList());
    }
    public Player getPlayer(Long id) {
        Optional<PlayerEntity> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return CoreConverters.toPlayer(player.get());
        } else {
            throw new NotFoundException();
        }
    }

    public boolean createPlayer(Player player) {
        Long id = player.getId();
        log.info("Creating player with id={}", id);
        boolean exists= playerRepository.existsById(id);
        if (exists) {
            log.info("Player with id={} already exists", id);
            return false;
        } else {
            PlayerEntity entity = CoreConverters.toPlayerEntity(player);
            PlayerEntity savedEntity = playerRepository.save(entity);
            log.info("Player with id={} has added", id);
            return true;
        }
    }

    public Player updatePlayer(Long playerId, Player player) {
        log.info("Updating player with id={}", playerId);
        Optional<PlayerEntity> getPlayer = playerRepository.findById(playerId);
        PlayerEntity existingPlayer = getPlayer
                .orElseThrow(() -> new NotFoundException("Player with id=" + playerId + " is not found"));

        existingPlayer.setFirstName(player.getFirstName());
        existingPlayer.setLastName(player.getLastName());
        existingPlayer.setUserName(player.getUsername());
        existingPlayer.setScore(player.getScore());
        existingPlayer.setGameScore(player.getGameScore() != null ? player.getGameScore() : player.getScore());

        return CoreConverters.toPlayer(playerRepository.save(existingPlayer));
    }

    public List<Player> getPlayersByIds(Collection<Long> ids) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(ids);

        return entities.stream().map(CoreConverters::toPlayer).collect(Collectors.toList());
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public void updatePlayerScores(Map<Long, Integer> playerPoints) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(playerPoints.keySet());
        entities.forEach((entity -> {entity.setGameScore(entity.getGameScore() + playerPoints.get(entity.getId()));}));

        playerRepository.saveAll(entities);
    }
}
