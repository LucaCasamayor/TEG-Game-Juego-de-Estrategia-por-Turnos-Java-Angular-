package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegionJpaRepository extends JpaRepository<RegionEntity, Long> {

    List<RegionEntity> getAllByMap_GameMapId(Long mapId);
}
