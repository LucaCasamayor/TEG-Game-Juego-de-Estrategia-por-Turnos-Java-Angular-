// repository.jpa.AiCharacterRepository.java
package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.AICharacterEntity;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiCharacterRepository extends JpaRepository<AICharacterEntity, Long> {
    List<AICharacterEntity> getAllByProfile(AIProfile profile);

    Optional<AICharacterEntity> findByCharacterId(Long characterId);
}
