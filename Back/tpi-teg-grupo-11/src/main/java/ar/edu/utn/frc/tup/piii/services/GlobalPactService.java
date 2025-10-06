package ar.edu.utn.frc.tup.piii.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalPactService {

//    private final GlobalPactPersistence globalPactPersistence;
//    private final ModelMapper modelMapper;

//
//    public List<GlobalPactDto> getAllGlobalPact() {
//        List<GlobalPact> globalPacts = globalPactPersistence.findAll();
//        return globalPacts.stream()
//                .map(p -> modelMapper.map(p, GlobalPactDto.class))
//                .collect(Collectors.toList());
//    }
//
//    public GlobalPactDto getGlobalPactByPlayer(Long playerId) {
//        PlayerDto playerDto = new PlayerDto();
//        playerDto.setPlayerId(playerId);
//        Player player = modelMapper.map(playerDto, Player.class);
//
//        List<GlobalPact> pacts = globalPactPersistence.findAllByPlayer(player);
//        if (pacts.isEmpty()) {
//            return null;
//        }
//        return modelMapper.map(pacts.get(0), GlobalPactDto.class);
//    }
//
//    public GlobalPactDto postGlobalPact(GlobalPactDto globalPactDto) {
//        GlobalPact globalPact = modelMapper.map(globalPactDto, GlobalPact.class);
//        GlobalPact saved = globalPactPersistence.updateGlobalPact(globalPact.getGlobalPactId(), globalPact);
//        return modelMapper.map(saved, GlobalPactDto.class);
//    }
//
//    public GlobalPactDto updateGlobalPact(GlobalPactDto globalPactDto) {
//        GlobalPact globalPact = modelMapper.map(globalPactDto, GlobalPact.class);
//        GlobalPact updated = globalPactPersistence.updateGlobalPact(globalPact.getGlobalPactId(), globalPact);
//        return modelMapper.map(updated, GlobalPactDto.class);
//    }
}
