package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import ar.edu.utn.frc.tup.piii.models.GameMap;
import ar.edu.utn.frc.tup.piii.persistence.GameMapPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameMapService {

    private final GameMapPersistence persistence;
    private final ModelMapper modelMapper;

    public List<GameMapDto> getAll() {
        return persistence.findAllGameMaps()
                .stream()
                .map(x -> modelMapper.map(x, GameMapDto.class))
                .collect(Collectors.toList());
    }

    public GameMapDto getById(Long id) {
        GameMap gameMap = persistence.findGameMapById(id);
        return modelMapper.map(gameMap, GameMapDto.class);
    }
}
