package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AICharacterDto {
    private Long characterId;
    private String name;
    private String imageUrl;
    private AIProfile profile;
    private String description;
}
