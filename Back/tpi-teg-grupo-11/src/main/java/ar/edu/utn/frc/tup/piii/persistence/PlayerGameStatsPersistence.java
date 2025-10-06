package ar.edu.utn.frc.tup.piii.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerGameStatsPersistence {
//
//    private final PlayerGameStatsJpaRepository repository;
//    private final ModelMapper modelMapper;
//
//    public PlayerGameStats saveStats(PlayerGameStats stats) {
//        PlayerGameStatsEntity entity = modelMapper.map(stats, PlayerGameStatsEntity.class);
//        return modelMapper.map(repository.save(entity), PlayerGameStats.class);
//    }
//    public PlayerGameStats updateStats(Long id, PlayerGameStats stats){
//        PlayerGameStatsEntity newStats = modelMapper.map(stats, PlayerGameStatsEntity.class);
//        PlayerGameStatsEntity oldStats = repository.getReferenceById(id);
//        oldStats.setArmiesDefeated(newStats.getArmiesDefeated());
//        oldStats.setArmiesHad(newStats.getArmiesHad());
//        oldStats.setArmiesLost(newStats.getArmiesLost());
//        oldStats.setCardsTraded(newStats.getCardsTraded());
//        oldStats.setTerritoriesConquered(newStats.getTerritoriesConquered());
//        oldStats.setTerritoriesLost(newStats.getTerritoriesLost());
//        oldStats.setPlayer(newStats.getPlayer());
//
//        return modelMapper.map(repository.save(oldStats), PlayerGameStats.class);
//    }
//
//    public void deleteStatsById(Long id){
//        repository.deleteById(id);
//    }
}
