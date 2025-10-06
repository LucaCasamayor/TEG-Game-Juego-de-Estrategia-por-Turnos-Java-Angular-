package ar.edu.utn.frc.tup.piii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerGameStats {
    private Long playerGameStatsId;
    private Player player;
    private int armiesHad;
    private int armiesLost;
    private int armiesDefeated;
    private int territoriesConquered;
    private int territoriesLost;
    private int cardsTraded;
}
