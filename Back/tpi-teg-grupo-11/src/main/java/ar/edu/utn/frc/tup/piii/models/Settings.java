package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    private Long settingsId;
    private int turnTime;
    private GameMap map;
    private AIProfile aiProfile;
    private List<ObjectiveType> objectiveTypes;
    private String password;
    private boolean isPrivate;
}
