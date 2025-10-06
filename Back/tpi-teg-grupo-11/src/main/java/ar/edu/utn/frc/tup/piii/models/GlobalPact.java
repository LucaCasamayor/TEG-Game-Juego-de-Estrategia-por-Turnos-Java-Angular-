package ar.edu.utn.frc.tup.piii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalPact {
    private Long globalPactId;
    private Player player1;
    private Player player2;
    private Game game;
    private boolean isActive;
}
