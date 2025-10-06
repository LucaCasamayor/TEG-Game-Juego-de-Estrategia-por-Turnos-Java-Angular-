package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryStateDto {
    private Long territoryStateId;
//    private int reinforcements;
    private TerritoryDto territory;
    private Long player;
    private int armyCount;
}
