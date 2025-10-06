package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDto {
    private Long id;
    private Long startTerritoryId;
    private Long endTerritoryId;
    private int armyCount;
    private List<DiceDto> dice;
    private TurnPhase movementType;
    // al crear un turno,
    // que se cree un movimiento con la cantidad de armyCount otorgada al jugador
}
