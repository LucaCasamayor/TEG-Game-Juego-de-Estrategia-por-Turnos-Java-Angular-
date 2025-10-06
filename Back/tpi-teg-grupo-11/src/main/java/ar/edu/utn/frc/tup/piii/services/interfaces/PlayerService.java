package ar.edu.utn.frc.tup.piii.services.interfaces;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    PlayerDto createPlayer(PlayerDto playerDto);
    List<PlayerDto> getPlayersByGame(UUID gameId);
    List<PlayerDto> getAll();
}
