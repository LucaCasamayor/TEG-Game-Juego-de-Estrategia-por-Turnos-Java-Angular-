package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.GameMapEntity;
import ar.edu.utn.frc.tup.piii.models.GameMap;
import ar.edu.utn.frc.tup.piii.repository.jpa.GameMapJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameMapPersistence {
    private final GameMapJpaRepository gameMapJpaRepository;
    private final ModelMapper modelMapper;

    public GameMap findGameMapById(Long gameMapId) {
        GameMapEntity gameMapEntity = gameMapJpaRepository.getReferenceById(gameMapId);
        return modelMapper.map(gameMapEntity, GameMap.class);
    }

    public List<GameMap> findAllGameMaps() {
        List<GameMapEntity> gameMapEntities = gameMapJpaRepository.findAll();
        return gameMapEntities.stream()
                .map(entity -> modelMapper.map(entity, GameMap.class))
                .collect(Collectors.toList());
    }
}
