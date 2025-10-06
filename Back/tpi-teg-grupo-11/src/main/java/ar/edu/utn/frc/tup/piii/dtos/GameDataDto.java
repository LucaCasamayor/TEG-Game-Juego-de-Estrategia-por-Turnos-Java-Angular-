package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDataDto {
    private UUID gameId;
    private List<TerritoryDto> territories;
    private List<PlayerDto> players;
    private GameState gameState;
    private int currentTurnIndex;
    private List<TurnDto> turns;
    private TurnDto currentTurn;
}
