package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Objective {
    private Long id;
    private String description;
    private ObjectiveType objectiveType;
    private Color color;
    private int firstRegionTerritoriesNeeded;
    private int secondRegionTerritoriesNeeded;
    private int thirdRegionTerritoriesNeeded;
    private int fourthRegionTerritoriesNeeded;
    private int fifthRegionTerritoriesNeeded;
    private int sixthRegionTerritoriesNeeded;
}
