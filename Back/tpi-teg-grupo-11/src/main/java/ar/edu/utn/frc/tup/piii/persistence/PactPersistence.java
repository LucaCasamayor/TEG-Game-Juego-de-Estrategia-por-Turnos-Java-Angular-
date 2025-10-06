package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.repository.jpa.PactJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PactPersistence {
    private final PactJpaRepository repository;
    private final ModelMapper modelMapper;

//    public Pact findById(Long id){
//        return modelMapper.map(repository.getReferenceById(id), Pact.class);
//    }
//    public List<Pact> findAllByGameId(UUID gameId){
//        List<PactEntity> pactEntities = repository.getAllByGame_GameId(gameId);
//        List<Pact> pacts = new ArrayList<>();
//        for (PactEntity pe : pactEntities){
//            pacts.add(modelMapper.map(pe, Pact.class));
//        }
//        return pacts;
//    }
//    public List<Pact> findPactByTerritoryId(Long id){
//        List<Pact> pacts = new ArrayList<>();
//        pacts.addAll(repository.getAllByTerritory1_TerritoryStateId(id).stream()
//                .map(entity -> modelMapper.map(entity, Pact.class))
//                .toList());
//        pacts.addAll(repository.getAllByTerritory2_TerritoryStateId(id).stream()
//                .map(entity -> modelMapper.map(entity, Pact.class))
//                .toList());
//        return pacts;
//    }
//    public Pact updatePact(Long pactId, Pact pact){
//        PactEntity oldPact = repository.getReferenceById(pactId);
//        PactEntity newPact = modelMapper.map(pact, PactEntity.class);
//
//        oldPact.setActive(newPact.isActive());
//        return modelMapper.map(repository.save(oldPact), Pact.class);
//    }
//
//    public Pact deletePact(Long pactId){
//        Pact deletedPact = modelMapper.map(repository.getReferenceById(pactId), Pact.class);
//        repository.deleteById(pactId);
//        return deletedPact;
//    }
}
