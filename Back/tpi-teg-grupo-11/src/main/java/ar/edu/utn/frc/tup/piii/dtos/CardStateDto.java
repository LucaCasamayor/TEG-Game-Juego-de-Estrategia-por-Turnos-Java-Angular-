package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStateDto{
    private Long cardStateId;
    private CardDto card;
    private GameDto game;
    private PlayerDto player;
    private boolean wasUsed;
}
