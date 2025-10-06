package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.enums.BotDifficulty;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import ar.edu.utn.frc.tup.piii.mappers.PlayerMapper;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.persistence.PlayerPersistence;
import ar.edu.utn.frc.tup.piii.persistence.UserPersistence;
import ar.edu.utn.frc.tup.piii.services.interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotPlayerServiceImpl implements PlayerService {

    private final ModelMapper modelMapper;
    private final PlayerPersistence persistence;
    private final UserPersistence userPersistence;
    private final PlayerMapper playerMapper;

    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player p = modelMapper.map(playerDto, Player.class);

        // Crear usuario bot
        User botUser = new User();
        botUser.setUserId(UUID.randomUUID());
        botUser.setUsername(botUser.getUserId().toString());
        botUser.setPassword("bot");
        botUser.setCreationDate(new Date());
        botUser.setImgUrl("assets/images/historical-heroes/"+getRandomImage()+".png");
        botUser.setActive(true);

        p.setPlayerType(PlayerType.BOT);
        User userSaved = userPersistence.save(botUser);
        p.setUser(userSaved);

        if (p.getDifficulty() == null) {
            p.setDifficulty(BotDifficulty.EASY);
        }

        if (p.getTerritories() == null) {
            p.setTerritories(new ArrayList<>());
        }

        if (p.getPlayerColor() == null) {
            List<Player> existingPlayers = persistence.findPlayersByGameId(p.getGameId());

            List<Color> usedColors = existingPlayers.stream()
                    .map(Player::getPlayerColor)
                    .filter(Objects::nonNull)
                    .toList();

            Color newColor = Arrays.stream(Color.values())
                    .filter(color -> !usedColors.contains(color))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No quedan colores disponibles para este juego"));

            p.setPlayerColor(newColor);
        }

        Player savedPlayer = persistence.createPlayer(p);
        return modelMapper.map(savedPlayer, PlayerDto.class);
    }

    @Override
    public List<PlayerDto> getPlayersByGame(UUID gameId) {
        return persistence.findPlayersByGameId(gameId).stream().map(playerMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PlayerDto> getAll() {
        return List.of();
    }

    private String getRandomImage() {
        Random random = new Random();
        String[] images = {"e", "f", "n", "o", "m", "l", "d"};

        int randomIndex = random.nextInt(images.length);
        return images[randomIndex];
    }

}
