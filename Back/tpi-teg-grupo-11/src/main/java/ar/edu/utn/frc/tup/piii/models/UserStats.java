package ar.edu.utn.frc.tup.piii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStats {
    private Long id;
    private User user;
    private int armiesHad;
    private int armiesLost;
    private int armiesDefeated;
    private int territoriesConquered;
    private int territoriesLost;
    private int gamesPlayed;
    private int gamesWon;
    private int roundsPlayed;
}
