package ar.edu.utn.frc.tup.piii.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatsDto {
    private Long id;
    private UserDto user;
    private int armiesHad;
    private int armiesLost;
    private int armiesDefeated;
    private int territoriesConquered;
    private int territoriesLost;
    private int gamesPlayed;
    private int gamesWon;
    private int roundsPlayed;
}
