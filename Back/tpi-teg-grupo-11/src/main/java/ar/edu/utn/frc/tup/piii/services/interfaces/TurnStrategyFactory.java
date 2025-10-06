package ar.edu.utn.frc.tup.piii.services.interfaces;

import ar.edu.utn.frc.tup.piii.enums.PlayerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class TurnStrategyFactory {

    private final Map<PlayerType, TurnService> strategyMap;

    @Autowired
    public TurnStrategyFactory(
            @Qualifier("turnHumanImpl") TurnService playerService,
            @Qualifier("turnBotEasyImpl") TurnService botEasyService
//            @Qualifier("turnBotMediumImpl") TurnService botMediumService,
//            @Qualifier("turnBotHardImpl") TurnService botHardService
    ) {
        strategyMap = new EnumMap<>(PlayerType.class);
        strategyMap.put(PlayerType.PLAYER, playerService);
        strategyMap.put(PlayerType.BOT, botEasyService);
//        strategyMap.put(PlayerType.BOT_MEDIUM, botMediumService);
//        strategyMap.put(PlayerType.BOT_HARD, botHardService);
//      SOLO Implementaremos la lógica de los bots fáciles
    }

    public TurnService getStrategy(PlayerType playerType) {
        return strategyMap.get(playerType);
    }
}

