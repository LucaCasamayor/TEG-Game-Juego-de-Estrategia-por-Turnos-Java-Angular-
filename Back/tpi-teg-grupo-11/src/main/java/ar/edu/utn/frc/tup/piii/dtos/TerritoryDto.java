package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerritoryDto {
    private Long territoryId;
    private String name;
    private RegionDto region;
    private List<Long> bordersId;
}
