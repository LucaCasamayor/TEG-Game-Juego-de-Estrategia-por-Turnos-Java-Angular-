package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalPactDto {
    private Long globalPactId;
    private PlayerDto player1;
    private PlayerDto player2;
    private GameDto game;
    private boolean isActive;
}
