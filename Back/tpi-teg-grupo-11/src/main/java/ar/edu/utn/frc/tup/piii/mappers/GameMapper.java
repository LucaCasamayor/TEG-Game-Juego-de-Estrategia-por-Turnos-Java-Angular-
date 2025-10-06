package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.GameDataDto;
import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.entities.GameEntity;
import ar.edu.utn.frc.tup.piii.models.Game;
import org.springframework.stereotype.Service;

@Service
public interface GameMapper {
    Game toModel(GameEntity entity);
    GameEntity toEntity(Game model);
    GameDto toDto(Game model);

    GameDataDto toGameData(Game game);
}
