package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import ar.edu.utn.frc.tup.piii.models.GameMap;
import ar.edu.utn.frc.tup.piii.persistence.GameMapPersistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameMapServiceTest {

    @Mock
    private GameMapPersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GameMapService gameMapService;

    @Test
    void getAll() {
        GameMap map1 = new GameMap();
        GameMap map2 = new GameMap();

        GameMapDto dto1 = new GameMapDto();
        GameMapDto dto2 = new GameMapDto();

        List<GameMap> gameMaps = List.of(map1, map2);

        Mockito.when(persistence.findAllGameMaps()).thenReturn(gameMaps);
        Mockito.when(modelMapper.map(map1, GameMapDto.class)).thenReturn(dto1);
        Mockito.when(modelMapper.map(map2, GameMapDto.class)).thenReturn(dto2);

        List<GameMapDto> result = gameMapService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(dto1, result.get(0));
        Assertions.assertEquals(dto2, result.get(1));
    }

    @Test
    void getById() {
        Long mapId = 1L;
        GameMap gameMap = new GameMap();
        GameMapDto dto = new GameMapDto();

        Mockito.when(persistence.findGameMapById(mapId)).thenReturn(gameMap);
        Mockito.when(modelMapper.map(gameMap, GameMapDto.class)).thenReturn(dto);

        GameMapDto result = gameMapService.getById(mapId);
        
        Assertions.assertEquals(dto, result);
    }
}