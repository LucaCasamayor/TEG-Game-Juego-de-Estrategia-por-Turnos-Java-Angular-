package ar.edu.utn.frc.tup.piii.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatsPersistence<UsersStats> {
//    private final UserStatsJpaRepository repository;
//    private final ModelMapper modelMapper;
//
//    public UserStats findByUser_UserId(Long userId) {
//        return modelMapper.map(repository.getReferenceById(userId), UserStats.class);
//    }
//
//    public List<UserStats> findAll() {
//        return modelMapper.map(repository.findAll(), List.class);
//    }
//
//    public UserStats createStats(UserStats stats) {
//        UserStatsEntity entity = modelMapper.map(stats, UserStatsEntity.class);
//        return modelMapper.map(repository.save(entity), UserStats.class);
//    }
//
//    public UserStats updateStats(Long id, UserStats stats) {
//        UserStatsEntity updEntity = repository.getReferenceById(id);
//        updEntity.setArmiesDefeated(stats.getArmiesDefeated());
//        updEntity.setArmiesLost(stats.getArmiesLost());
//        updEntity.setArmiesHad(stats.getArmiesHad());
//        updEntity.setTerritoriesConquered(stats.getTerritoriesConquered());
//        updEntity.setTerritoriesLost(stats.getTerritoriesLost());
//        updEntity.setGamesPlayed(stats.getGamesPlayed());
//        updEntity.setGamesWon(stats.getGamesWon());
//        updEntity.setRoundsPlayed(stats.getRoundsPlayed());
//        return modelMapper.map(repository.save(updEntity), UserStats.class);
//    }
}