package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Turn {
    private Long turnId;
    private UUID gameId;
    private TurnPhase turnPhase;
    private Player player;
    private List<Movement> movements;
    private Integer roundNumber ;
}
