package ar.edu.utn.frc.tup.piii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryState {

    private Long territoryStateId;
    private Long playerId;
    private int armyCount;
    private Territory territory;
    // private int reinforcements
}
