package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.entities.MovementEntity;
import ar.edu.utn.frc.tup.piii.models.Movement;

public interface MovementMapper {
    MovementEntity toEntity(Movement model);
}
