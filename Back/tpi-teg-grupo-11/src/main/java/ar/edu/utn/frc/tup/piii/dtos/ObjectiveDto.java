package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectiveDto {
    private Long objectiveId;
    private String description;
    private ObjectiveTypeDto objectiveType;
    private Color color;
    private int firstRegionTerritoriesNeeded;
    private int secondRegionTerritoriesNeeded;
    private int thirdRegionTerritoriesNeeded;
    private int fourthRegionTerritoriesNeeded;
    private int fifthRegionTerritoriesNeeded;
    private int sixthRegionTerritoriesNeeded;
}
