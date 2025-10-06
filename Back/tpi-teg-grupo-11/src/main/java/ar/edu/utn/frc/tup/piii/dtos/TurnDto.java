package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnDto {
    private Long turnId;
    private PlayerDto player;
//    private GameDto game;
    private TurnPhase turnPhase;
    private List<MovementDto> movements;
}
