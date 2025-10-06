package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectiveTypeDto {
    private Long objectiveTypeId;
    private String name;
    private List<ObjectiveDto> objectives;
}
