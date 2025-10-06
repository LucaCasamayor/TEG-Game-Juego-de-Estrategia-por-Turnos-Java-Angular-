package ar.edu.utn.frc.tup.piii.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementService {
//
//    private final AchievementPersistence achievementPersistence;
//    private final ModelMapper modelMapper;
//
//    public AchievementService(AchievementPersistence achievementPersistence, ModelMapper modelMapper) {
//        this.achievementPersistence = achievementPersistence;
//        this.modelMapper = modelMapper;
//    }
//
//    public List<AchievementDto> getAllAchievements() {
//        List<Achievement> achievements = achievementPersistence.findAllAchievements();
//        return achievements.stream()
//                .map(achievement -> modelMapper.map(achievement, AchievementDto.class))
//                .collect(Collectors.toList());
//    }
//
//    public AchievementDto getAchievementById(Long id) {
//        Achievement achievement = achievementPersistence.findByAchievementId(id);
//        return modelMapper.map(achievement, AchievementDto.class);
//    }
//
//    public AchievementDto getAchievementByName(String achievementName) {
//        Achievement achievement = achievementPersistence.findByName(achievementName);
//        if (achievement == null) {
//            return null;
//        }
//        return modelMapper.map(achievement, AchievementDto.class);
//    }
//
//
//    public AchievementDto postAchievement(AchievementDto achievement) {
//        Achievement saved = achievementPersistence.saveAchievement(modelMapper.map(achievement, Achievement.class));
//        return modelMapper.map(saved, AchievementDto.class);
//    }
}
