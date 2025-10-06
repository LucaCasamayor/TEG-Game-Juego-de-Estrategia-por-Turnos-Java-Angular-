package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.RegionEntity;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.services.interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HumanPlayerServiceImpl implements PlayerService {

    private final PlayerPersistence persistence;
    private final ModelMapper modelMapper;


    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = modelMapper.map(playerDto, Player.class);

        if (player.getGameId() == null) {
            throw new RuntimeException("El juego es requerido para crear un jugador");
        }

        List<Player> existingPlayers = persistence.findPlayersByGameId(player.getGameId());

        List<Color> usedColors = existingPlayers.stream()
                .map(Player::getPlayerColor)
                .collect(Collectors.toList());

        Color newColor = Arrays.stream(Color.values())
                .filter(color -> !usedColors.contains(color))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No quedan colores disponibles para este juego"));

        player.setPlayerColor(newColor);

        Player savedPlayer = persistence.createPlayer(player);

        return modelMapper.map(savedPlayer, PlayerDto.class);
    }


    @Override
    public List<PlayerDto> getPlayersByGame(UUID gameId) {
        return persistence.findPlayersByGameId(gameId)
                .stream()
                .map(x -> modelMapper.map(x, PlayerDto.class))
                .collect(Collectors.toList());
    }


//    //TODO adaptar
//    private int getRegionBonus(RegionEntity region) {
//        return switch (region.getName()) {
//            case "América del Norte", "Europa" -> 5;
//            case "América del Sur", "Oceanía" -> 2;
//            case "África" -> 3;
//            case "Asia" -> 7;
//            default -> 0;
//        };
//    }

    @Override
    public List<PlayerDto> getAll() {
        return persistence.findAll();
    }

}
