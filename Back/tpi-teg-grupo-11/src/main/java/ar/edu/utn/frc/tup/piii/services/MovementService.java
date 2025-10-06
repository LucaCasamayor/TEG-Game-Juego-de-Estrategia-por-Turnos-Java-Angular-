package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.MovementDto;
import ar.edu.utn.frc.tup.piii.models.Movement;
import ar.edu.utn.frc.tup.piii.persistence.MovementPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementPersistence persistence;
    private final ModelMapper modelMapper;


//    public List<MovementDto> getAllByGame(UUID gameId) {
//        return persistence.getAllByGameId(gameId).stream()
//                .map(movement -> modelMapper.map(movement, MovementDto.class))
//                .collect(Collectors.toList());
//    }

    public MovementDto saveMovement(MovementDto movement) {
        return modelMapper.map(persistence.saveMovement(modelMapper.map(movement, Movement.class)), MovementDto.class);
    }

    public MovementDto getMovementById(Long movementId) {
        return modelMapper.map(persistence.getMovementById(movementId), MovementDto.class);
    }

    public List<MovementDto> getAllByGame(String gameId) {
        return persistence.findAllByGameId(gameId).stream()
                .map(mov -> modelMapper.map(mov, MovementDto.class))
                .toList();
    }


}
