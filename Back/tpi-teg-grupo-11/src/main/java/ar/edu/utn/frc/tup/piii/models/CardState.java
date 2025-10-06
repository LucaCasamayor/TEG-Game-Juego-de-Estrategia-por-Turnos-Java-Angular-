package ar.edu.utn.frc.tup.piii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardState {
    private Long cardStateId;
    private Card card;
    private Game game;
    private Player player;
    private boolean wasUsed;
}
