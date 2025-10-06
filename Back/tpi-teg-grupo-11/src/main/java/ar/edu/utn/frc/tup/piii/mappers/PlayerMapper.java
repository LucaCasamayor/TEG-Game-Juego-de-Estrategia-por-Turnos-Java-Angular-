package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.models.Player;

public interface PlayerMapper {
    Player toModel(PlayerDto dto);
    Player toModel(PlayerEntity entity);
    PlayerEntity toEntity(Player model);
    PlayerDto toDto(Player model);
}
