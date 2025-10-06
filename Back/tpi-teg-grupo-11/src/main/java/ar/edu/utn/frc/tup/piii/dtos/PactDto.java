package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PactDto {
    private Long id;
    private TerritoryDto territory1;
    private TerritoryDto territory2;
    private GameDto game;
    private boolean isActive;
}
