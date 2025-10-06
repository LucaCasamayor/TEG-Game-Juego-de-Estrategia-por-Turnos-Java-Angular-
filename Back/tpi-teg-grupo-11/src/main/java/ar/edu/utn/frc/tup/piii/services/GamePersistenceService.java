package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.models.Game;
import ar.edu.utn.frc.tup.piii.persistence.GamePersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GamePersistenceService {
    private final GamePersistence persistence;
    private final ModelMapper modelMapper;

    public GameDto updateGame(UUID gameId, GameDto gameDto) {
        return modelMapper.map(persistence.updateGame(gameId, modelMapper.map(gameDto, Game.class)), GameDto.class);
    }

    public Game getGameById(UUID gameId){
        return persistence.getGameById(gameId);
    }
}
