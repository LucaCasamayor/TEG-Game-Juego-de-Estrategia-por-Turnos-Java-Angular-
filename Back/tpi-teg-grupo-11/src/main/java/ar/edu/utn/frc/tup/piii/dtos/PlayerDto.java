package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    private Long playerId;
    private UserDto user;
    private UUID gameId;
    private ObjectiveDto objective;
    private Color playerColor;
    private int turnOrder;
    @JsonProperty("isWinner")
    private boolean isWinner;
    private boolean hasLost;
    private BotDifficulty difficulty;
    private PlayerType playerType;
    private List<TerritoryStateDto> territories;
}
