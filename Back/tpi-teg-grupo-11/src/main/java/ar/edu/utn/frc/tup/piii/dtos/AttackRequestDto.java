package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttackRequestDto {
    private TerritoryStateDto from;
    private TerritoryStateDto to;
    private int armyCount;
}

