package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.dtos.GameMapDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    private Long id;
    private String name;
    private int numberOfTerritories;
    private GameMapDto map;
    private List<Territory> territories;
}
