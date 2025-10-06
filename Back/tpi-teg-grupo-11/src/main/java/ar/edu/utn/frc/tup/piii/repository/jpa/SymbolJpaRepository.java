package ar.edu.utn.frc.tup.piii.repository.jpa;

import ar.edu.utn.frc.tup.piii.entities.SymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolJpaRepository extends JpaRepository<SymbolEntity, Long> {
    SymbolEntity findSymbolBySymbolId(Long symbolId);
}
