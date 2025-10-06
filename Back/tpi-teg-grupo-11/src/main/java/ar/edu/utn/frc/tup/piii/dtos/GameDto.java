package ar.edu.utn.frc.tup.piii.dtos;

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
public class GameDto {
    private UUID gameId;
    private SettingsDto settings;
    private List<PlayerDto> players;
    private Date creationDate;
    private GameState gameState;
}