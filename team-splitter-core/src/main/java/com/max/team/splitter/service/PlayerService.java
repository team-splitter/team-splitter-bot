package com.max.team.splitter.service;

import com.max.team.splitter.converter.Converters;
import com.max.team.splitter.exception.NotFoundException;
import com.max.team.splitter.model.Player;
import com.max.team.splitter.persistence.entities.PlayerEntity;
import com.max.team.splitter.persistence.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.max.team.splitter.converter.Converters.toPlayer;
import static com.max.team.splitter.converter.Converters.toPlayerEntity;

@Service
public class PlayerService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public Player getPlayer(Long id) {
        Optional<PlayerEntity> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return toPlayer(player.get());
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
            PlayerEntity entity = toPlayerEntity(player);
            PlayerEntity savedEntity = playerRepository.save(entity);
            log.info("Player with id={} has added", id);
            return true;
        }
    }

    public List<Player> getPlayersByIds(List<Long> ids) {
        List<PlayerEntity> entities = playerRepository.findByIdIn(ids);

        return entities.stream().map(Converters::toPlayer).collect(Collectors.toList());
    }

}
