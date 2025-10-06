package ar.edu.utn.frc.tup.piii.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AchievementPersistence {
//
//    private final AchievementJpaRepository repository;
//    private final ModelMapper modelMapper;

//    public List<Achievement> findAllAchievements() {
//        List<AchievementEntity> lstEntity = repository.findAll();
//        List<Achievement> lstModel = new ArrayList<>();
//        for(AchievementEntity entity : lstEntity){
//            lstModel.add(modelMapper.map(entity, Achievement.class));
//        }
//        return lstModel;
//    }
//
//    public Achievement findByAchievementId(Long achievementId) {
//        AchievementEntity entity = repository.findById(achievementId)
//                .orElseThrow(() -> new EntityNotFoundException("Achievement not found with id: " + achievementId));
//        return modelMapper.map(entity, Achievement.class);
//    }
//    public Achievement findByName(String name) {
//        Optional<AchievementEntity> optionalEntity = repository.findByName(name);
//        AchievementEntity entity = optionalEntity.orElse(null);
//        if (entity == null) {
//            return null;
//        }
//        return modelMapper.map(entity, Achievement.class);
//    }
//
//    public Achievement saveAchievement(Achievement model) {
//        AchievementEntity entity = modelMapper.map(model, AchievementEntity.class);
//        AchievementEntity saved = repository.save(entity);
//        return modelMapper.map(saved, Achievement.class);
//    }
//
//    public void deleteAchievement(Long id) {
//        repository.deleteById(id);

}

