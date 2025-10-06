package ar.edu.utn.frc.tup.piii.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAchievementPersistence {
//
//    private final UserAchievementJpaRepository repository;
//    private final ModelMapper modelMapper;
//
//    public List<UserAchievement> findByUser_UserId(UUID userId) {
//        List<UserAchievementEntity> entities = repository.findAllByUser_UserId(userId);
//        return entities.stream()
//                .map(entity -> modelMapper.map(entity, UserAchievement.class))
//                .collect(Collectors.toList());
//    }
//
//    public UserAchievement saveUserAchievement(UserAchievement achievement) {
//        UserAchievementEntity entity = modelMapper.map(achievement, UserAchievementEntity.class);
//        return modelMapper.map(repository.save(entity), UserAchievement.class);
//    }

}
