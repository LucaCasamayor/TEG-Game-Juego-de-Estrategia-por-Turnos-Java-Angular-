package ar.edu.utn.frc.tup.piii.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PactService {
//
//    private final PactPersistence persistence;
//    private final ModelMapper modelMapper;

//    public List<PactDto> getAllByGameId(UUID gameId) {
//        return persistence.findAllByGameId(gameId).stream()
//                .map(pact -> modelMapper.map(pact, PactDto.class))
//                .collect(Collectors.toList());
//    }
//
//    public List<PactDto> getAllActiveByGameId(UUID gameId){
//
//        List<Pact> pacts = persistence.findAllByGameId(gameId);
//        pacts.removeIf(p -> !p.isActive());
//        return pacts.stream()
//                .map(pact -> modelMapper.map(pact, PactDto.class))
//                .collect(Collectors.toList());
//    }
//
//    public List<PactDto> getByTerritoryId(Long territoryId) {
//        return persistence.findPactByTerritoryId(territoryId)
//                .stream()
//                .map(pact -> modelMapper.map(pact, PactDto.class))
//                .collect(Collectors.toList());
//    }
//
//    public PactDto updatePact(Long pactId, PactDto pact) {
//        return modelMapper.map(persistence.updatePact(pactId, modelMapper.map(pact, Pact.class)), PactDto.class);
//    }
//    public PactDto deletePact(Long pactId){
//        return modelMapper.map(persistence.deletePact(pactId), PactDto.class);
//    }
}
