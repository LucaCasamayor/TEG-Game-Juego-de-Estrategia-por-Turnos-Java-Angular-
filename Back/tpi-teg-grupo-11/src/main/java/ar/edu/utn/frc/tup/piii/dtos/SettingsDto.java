package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDto {
    private Long settingsId;
    private int turnTime;
    private GameMapDto map;
    private AIProfile aiProfile;
    private List<ObjectiveTypeDto> objectiveTypes;
    private String password;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
}
