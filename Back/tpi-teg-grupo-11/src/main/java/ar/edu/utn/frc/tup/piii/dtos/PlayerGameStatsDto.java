package ar.edu.utn.frc.tup.piii.dtos;

import ar.edu.utn.frc.tup.piii.models.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerGameStatsDto {
    private Long playerGameStatsId;
    private Player player;
    private int armiesHad;
    private int armiesLost;
    private int armiesDefeated;
    private int territoriesConquered;
    private int territoriesLost;
    private int cardsTraded;
}
