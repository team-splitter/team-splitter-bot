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

    public boolean addPlayer(Player player) {
        Long id = player.getId();
        log.info("Adding player with id={}", id);
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

    public List<Player> getPlayersByIds(Collection<Long> ids) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(ids);

        return entities.stream().map(CoreConverters::toPlayer).collect(Collectors.toList());
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

}
