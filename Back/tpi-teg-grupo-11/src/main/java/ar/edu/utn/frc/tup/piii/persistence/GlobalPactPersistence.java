package ar.edu.utn.frc.tup.piii.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalPactPersistence {
//    private final GlobalPactJpaRepository repository;
//    private final ModelMapper modelMapper;
//
//
//    public GlobalPact findById(Long id){
//        return modelMapper.map(repository.getReferenceById(id), GlobalPact.class);
//    }
//
//    public List<GlobalPact> findAll(){
//        List<GlobalPactEntity> globalPactEntities = repository.findAll();
//        List<GlobalPact> globalPacts = new ArrayList<>();
//        for(GlobalPactEntity gpe : globalPactEntities){
//            globalPacts.add(modelMapper.map(gpe, GlobalPact.class));
//        }
//        return globalPacts;
//    }
//    public List<GlobalPact> findAllByPlayer(Player player){
//        PlayerEntity pe = modelMapper.map(player, PlayerEntity.class);
//        List<GlobalPactEntity> globalPactEntities = repository.findAllByPlayer1(pe);
//        List<GlobalPact> globalPacts = new ArrayList<>();
//        for(GlobalPactEntity gpe : globalPactEntities){
//            globalPacts.add(modelMapper.map(gpe, GlobalPact.class));
//        }
//        globalPactEntities = repository.findAllByPlayer2(pe);
//        for(GlobalPactEntity gpe : globalPactEntities){
//            globalPacts.add(modelMapper.map(gpe, GlobalPact.class));
//        }
//        return globalPacts;
//    }
//    public GlobalPact updateGlobalPact(Long id, GlobalPact pact){
//        GlobalPactEntity oldPact = repository.getReferenceById(id);
//        GlobalPactEntity newPact = modelMapper.map(pact, GlobalPactEntity.class);
//        oldPact.setPlayer1(newPact.getPlayer1());
//        oldPact.setPlayer2(newPact.getPlayer2());
//        oldPact.setActive(newPact.isActive());
//        return modelMapper.map(repository.save(oldPact), GlobalPact.class);
//    }
}
