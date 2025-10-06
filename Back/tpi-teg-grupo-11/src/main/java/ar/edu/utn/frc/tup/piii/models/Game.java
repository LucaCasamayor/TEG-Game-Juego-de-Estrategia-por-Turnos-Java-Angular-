package ar.edu.utn.frc.tup.piii.models;

import ar.edu.utn.frc.tup.piii.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private UUID gameId;
    private Settings settings;
    private Date creationDate;
    private GameState gameState;
    private List<Player> players;
    private List<Turn> turns;
    private List<CardState> cardStates;
//    private List<Pact> pacts;
//    private List<GlobalPact> globalPacts;
    private List<Territory> territories;
}