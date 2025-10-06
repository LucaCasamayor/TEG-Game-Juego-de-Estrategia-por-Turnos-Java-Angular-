package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    private Long id;
    private Long startTerritoryId;
    private Long endTerritoryId;
    private TurnPhase movementType;
    private int armyCount;
    private List<Dice> dice;
}
