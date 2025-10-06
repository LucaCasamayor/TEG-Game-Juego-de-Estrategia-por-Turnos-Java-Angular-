package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.entities.CardStateEntity;
import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Long playerId;
    private UUID gameId;
    private User user;
    private Objective objective;
    private Color playerColor;
    private int turnOrder;
    private boolean isWinner;
    private boolean hasLost;
    private BotDifficulty difficulty;
    private PlayerType playerType;
    private List<CardStateEntity> cards;
//    private List<GlobalPactEntity> globalPactsStarted;
//    private List<GlobalPactEntity> globalPactsAccepted;
    private List<TerritoryState> territories;
}