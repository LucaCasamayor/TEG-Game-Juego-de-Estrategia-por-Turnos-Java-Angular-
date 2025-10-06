package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.GameDataDto;
import ar.edu.utn.frc.tup.piii.dtos.GameDto;
import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.dtos.PlayerDto;
import ar.edu.utn.frc.tup.piii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.enums.Color;
import ar.edu.utn.frc.tup.piii.enums.GameState;
import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import ar.edu.utn.frc.tup.piii.enums.TurnPhase;
import ar.edu.utn.frc.tup.piii.mappers.GameMapper;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.*;
import ar.edu.utn.frc.tup.piii.persistence.*;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import ar.edu.utn.frc.tup.piii.services.implementations.TurnServiceCommon;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ar.edu.utn.frc.tup.piii.enums.GameState.IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GamePersistence persistence;
    private final PlayerPersistence playerPersistence;
    private final ModelMapper modelMapper;
    private final ObjectiveService objectiveService;
    private final TerritoryPersistence territoryPersistence;
    private final GamePersistence gamePersistence;
    private final UserJpaRepository userJpaRepository;
    private final TerritoryStatePersistence territoryStatePersistence;
    private final GameMapper gameMapper;
    private final TurnStrategyFactory turnStrategyFactory;
    private final UserPersistence userPersistence;
    private final TurnServiceCommon turnService;
    private final TerritoryStateMapper territoryStateMapper;
    private final TurnServiceCommon turnServiceCommon;

    public GameDataDto findGameData(UUID gameId) {
        return persistence.findGameDataById(gameId);
    }

    public GameDto findGame(UUID gameId) {
        return persistence.findGameById(gameId);
    }

    public GameDto joinGame(UUID gameId, UUID userId, String password) {
        Game game = gamePersistence.getGameById(gameId);

        if (game.getGameState().equals(IN_PROGRESS)) {
            throw new IllegalArgumentException("La partida ya está en curso");
        }

        if (game.getSettings().isPrivate() && password == null) {
            throw new IllegalArgumentException("La partida es privada");
        }


        if (game.getSettings().isPrivate() && !game.getSettings().getPassword().equals(password)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        boolean alreadyJoined = game.getPlayers().stream()
                .anyMatch(p -> p.getUser().getUserId().equals(userId));

        if (alreadyJoined) {
            throw new IllegalArgumentException("Usuario ya está en la partida");
        }

        User user = userPersistence.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }


        Player player = new Player();
        player.setUser(user);
        player.setGameId(game.getGameId());
        player.setPlayerType(PlayerType.PLAYER);
        player.setTurnOrder(game.getPlayers().size() + 1);

        Player persistedPlayer = playerPersistence.createPlayer(player);

        List<Player> gamePlayers = new ArrayList<>(game.getPlayers());
        gamePlayers.add(persistedPlayer);
        game.setPlayers(gamePlayers);

        gamePersistence.updateGame(gameId, game);

        return gameMapper.toDto(game);
    }

    public GameDto createGame(GameDto gameDto) {
        Game game = modelMapper.map(gameDto, Game.class);

        List<Player> processedPlayers = game.getPlayers().stream()
                .map(player -> {
                    if (player.getPlayerId() != null) {
                        // Ya existe
                        return player;
                    }

                    UUID userId = player.getUser().getUserId();
                    UserEntity userEntity = userJpaRepository.findByUserId(userId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

                    PlayerEntity playerEntity = new PlayerEntity();
                    playerEntity.setUser(userEntity);

                    return modelMapper.map(playerEntity, Player.class);
                }).toList();

        game.setPlayers(processedPlayers);

        Game savedGame = persistence.createGame(game);

        return modelMapper.map(savedGame, GameDto.class);
    }

    public Game startGame(UUID gameId) {
        Game gameExistent = persistence.getGameById(gameId);
        List<Player> players = gameExistent.getPlayers();

        List<Territory> territories = territoryPersistence.findAllTerritories();

        List<ObjectiveTypeDto> objectiveTypes = gameExistent.getSettings().getObjectiveTypes()
                .stream().map(objType -> modelMapper.map(objType, ObjectiveTypeDto.class))
                .collect(Collectors.toList());

        List<Objective> objectives = objectiveService.getAllByObjectiveTypes(objectiveTypes)
                .stream().map(x -> modelMapper.map(x, Objective.class))
                .toList();

        List<Color> playersColors = players.stream().map(Player::getPlayerColor).toList();

        List<Objective> filteredObjectives = objectives.stream()
                .filter(obj -> obj.getColor() == null || playersColors.contains(obj.getColor()))
                .collect(Collectors.toList());

        Collections.shuffle(filteredObjectives);

        assignObjectives(players, filteredObjectives);
        assignTerritories(territories, gameExistent, players);

        Turn firstTurn = new Turn();
        firstTurn.setGameId(gameExistent.getGameId());
        firstTurn.setPlayer(players.get(0));
        Turn persistedTurn = turnService.createTurn(firstTurn);

        gameExistent = persistence.getGameById(gameExistent.getGameId());
        gameExistent.setTerritories(territories);
        gameExistent.setGameState(IN_PROGRESS);

        return persistence.updateGame(gameId, gameExistent);
    }

    private void assignTerritories(List<Territory> territories, Game gameExistent, List<Player> players) {
        for (Territory territory : territories) {
            TerritoryState territoryState = new TerritoryState();
            territoryState.setTerritory(territory);
            territoryState.setArmyCount(0);
            territoryState.setPlayerId(gameExistent.getPlayers().get(0).getPlayerId());
            TerritoryState saved = territoryStatePersistence.createTerritoryState(territoryState);
        }
        List<TerritoryState> allStates = territoryStatePersistence.findTerritoryStateByPlayer(gameExistent.getPlayers().get(0).getPlayerId());

        Collections.shuffle(allStates);

        int numPlayers = players.size();
        for (int i = 0; i < territories.size(); i++) { // Repartir los TerritoryState entre los jugadores
            Player player = players.get(i % numPlayers);
            TerritoryState territoryState = allStates.get(i);
            territoryState.setPlayerId(player.getPlayerId());
            territoryState.setArmyCount(1);

            TerritoryState saved = territoryStatePersistence.updateTerritoryState(territoryState.getTerritoryStateId(), territoryState);

            if (player.getTerritories() == null) {
                player.setTerritories(new ArrayList<>());
            }
            player.getTerritories().add(saved);
            allStates.set(i, saved);
        }
    }

    private void assignObjectives(List<Player> players, List<Objective> filteredObjectives) {
        int order = 1;
        for (Player player : players) {
            player.setTurnOrder(order++);

            Objective assigned = null;
            for (Objective obj : filteredObjectives) {
                if (obj.getColor() == null || !obj.getColor().equals(player.getPlayerColor())) {
                    assigned = obj;
                    break;
                }
            }

            if (assigned != null) {
                player.setObjective(assigned);
                filteredObjectives.remove(assigned);
            }

            playerPersistence.updatePlayer(player.getPlayerId(), player);
        }
    }

    public GameDto updateGameState(UUID gameId, GameState state) {
        Game game = persistence.getGameById(gameId);

        Game modifiedGame = switch (state) {
            case WAITING_PLAYERS -> startGame(gameId);
            case IN_PROGRESS, PAUSED ->
                // TODO temporaralmente devolver el juego para evitar null
                    game;
            default -> throw new RuntimeException("Estado no manejado: " + state);
        };


        return gameMapper.toDto(modifiedGame);
    }

    //falta logica
    private Game endGame(UUID gameId, Game game) {
        game.setGameState(GameState.FINISHED);
        game = gamePersistence.updateGame(gameId, game);
        List<Player> players = game.getPlayers();
        for(Player p : players){
            if(!p.isWinner()){
                p.setHasLost(true);
                playerPersistence.updatePlayer(p.getPlayerId(), p);
            }
        }
        return game;
    }

    public GameDataDto updateGameInGame(UUID gameId, GameDataDto gameDataDto) throws Exception {
        return playCurrentPhase(gameId, gameDataDto);
    }

    public GameDataDto playCurrentPhase(UUID gameId, GameDataDto gameDataDto) throws Exception {

        Game game = persistence.getGameById(gameId);

        Turn modifiedCurrentTurn = modelMapper.map(gameDataDto.getCurrentTurn(), Turn.class);

        Player p  = modifiedCurrentTurn.getPlayer();
        p.setTerritories(temporalTerritoryStateNullFix(gameDataDto, p));
        modifiedCurrentTurn.setPlayer(p);

        modifiedCurrentTurn.setGameId(game.getGameId());

        TurnService turnService = switch (modifiedCurrentTurn.getPlayer().getPlayerType()) {
            case PLAYER -> turnStrategyFactory.getStrategy(PlayerType.PLAYER);
            case BOT -> turnStrategyFactory.getStrategy(PlayerType.BOT);
        };

        TurnPhase turnPhasePrePlay = modifiedCurrentTurn.getTurnPhase();
        switch (turnPhasePrePlay) {
            case DEPLOYMENT ->  turnService.playDeployStage(modifiedCurrentTurn);
            case ATTACK ->  turnService.playAttackStage(modifiedCurrentTurn);
            case FORTIFY ->  turnService.playFortifyStage(modifiedCurrentTurn);
            case CARD ->  turnService.playCardStage(modifiedCurrentTurn);
        }



        Game updatedGame = persistence.getGameById(gameId);

        updatedGame = checkForLosers(updatedGame);
        modifiedCurrentTurn = turnServiceCommon.checkObjectiveStates(modifiedCurrentTurn);
        updatedGame = persistence.getGameById(gameId);
        if(turnPhasePrePlay == TurnPhase.FORTIFY){
            if( checkForWinner(updatedGame)){
                Game endedGame = endGame(updatedGame.getGameId(), updatedGame);
                return gameMapper.toGameData(endedGame);
            }
        }


        return gameMapper.toGameData(updatedGame);
    }

    private Game checkForLosers(Game updatedGame) {
        for(Player p : updatedGame.getPlayers()){
            if(p.getTerritories().isEmpty()){
                p.setHasLost(true);
                playerPersistence.updatePlayer(p.getPlayerId(), p);
            }
        }
        return gamePersistence.getGameById(updatedGame.getGameId());
    }

    private boolean checkForWinner(Game updatedGame) {
        for(Player p : updatedGame.getPlayers()){
            if(p.isWinner()){
                return true;
            }
        }
        return false;
    }

    public List<TerritoryState> temporalTerritoryStateNullFix(GameDataDto game, Player player ){
        for(PlayerDto p: game.getPlayers()){
            if(p.getPlayerId().equals(player.getPlayerId())){
                return p.getTerritories().stream().map(territoryStateMapper::toModel).collect(Collectors.toList());
            }
        }
        return null;
    }

    public GameDataDto nextPhase(UUID gameId, GameDataDto gameDataDto) {
        Game game = persistence.getGameById(gameId);

        Turn modifiedCurrentTurn = modelMapper.map(gameDataDto.getCurrentTurn(), Turn.class);

        Player p  = modifiedCurrentTurn.getPlayer();
        p.setTerritories(temporalTerritoryStateNullFix(gameDataDto, p));
        modifiedCurrentTurn.setPlayer(p);

        modifiedCurrentTurn.setGameId(game.getGameId());

        Turn turn = turnServiceCommon.updateTurnPhase(modifiedCurrentTurn);

        Game updatedGame = persistence.getGameById(gameId);

        return gameMapper.toGameData(updatedGame);
    }
}

