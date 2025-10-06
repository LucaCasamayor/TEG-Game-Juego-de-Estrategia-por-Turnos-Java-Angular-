package ar.edu.utn.frc.tup.piii.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import ar.edu.utn.frc.tup.piii.models.Movement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ar.edu.utn.frc.tup.piii.dtos.MovementDto;
import ar.edu.utn.frc.tup.piii.persistence.MovementPersistence;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @InjectMocks
    private MovementService movementService;

    @Mock
    private MovementPersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void saveMovement_shouldMapAndPersistMovement() {
        MovementDto inputDto = new MovementDto();
        Movement movement = new Movement();
        Movement savedMovement = new Movement();
        MovementDto resultDto = new MovementDto();

        Mockito.when(modelMapper.map(inputDto, Movement.class)).thenReturn(movement);
        Mockito.when(persistence.saveMovement(movement)).thenReturn(savedMovement);
        Mockito.when(modelMapper.map(savedMovement, MovementDto.class)).thenReturn(resultDto);

        MovementDto result = movementService.saveMovement(inputDto);

        assertEquals(resultDto, result);
        verify(modelMapper).map(inputDto, Movement.class);
        verify(persistence).saveMovement(movement);
        verify(modelMapper).map(savedMovement, MovementDto.class);
    }

    @Test
    void getMovementById_shouldReturnMappedDto() {
        Long movementId = 1L;
        Movement movement = new Movement();
        MovementDto dto = new MovementDto();

        Mockito.when(persistence.getMovementById(movementId)).thenReturn(movement);
        Mockito.when(modelMapper.map(movement, MovementDto.class)).thenReturn(dto);

        MovementDto result = movementService.getMovementById(movementId);

        assertEquals(dto, result);
        verify(persistence).getMovementById(movementId);
        verify(modelMapper).map(movement, MovementDto.class);
    }

    @Test
    void getAllByGame_shouldReturnListOfDtos() {
        String gameId = "123";

        // Create distinct movements to avoid mock collision
        Movement movement1 = new Movement();
        movement1.setId(1L); // Set different IDs to make them distinct

        Movement movement2 = new Movement();
        movement2.setId(2L);

        List<Movement> movements = List.of(movement1, movement2);

        MovementDto dto1 = new MovementDto();
        MovementDto dto2 = new MovementDto();

        Mockito.when(persistence.findAllByGameId(gameId)).thenReturn(movements);

        // Use any() matcher or set up mocks for the specific objects
        Mockito.when(modelMapper.map(any(Movement.class), eq(MovementDto.class)))
                .thenReturn(dto1, dto2); // Return dto1 first, then dto2

        List<MovementDto> result = movementService.getAllByGame(gameId);

        assertEquals(2, result.size());
        assertEquals(List.of(dto1, dto2), result);

        verify(persistence).findAllByGameId(gameId);
        verify(modelMapper, times(2)).map(any(Movement.class), eq(MovementDto.class));
    }
}