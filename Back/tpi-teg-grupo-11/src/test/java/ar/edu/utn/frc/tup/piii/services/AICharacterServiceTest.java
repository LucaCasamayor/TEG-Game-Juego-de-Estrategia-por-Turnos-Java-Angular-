package ar.edu.utn.frc.tup.piii.services;
import ar.edu.utn.frc.tup.piii.dtos.AICharacterDto;
import ar.edu.utn.frc.tup.piii.entities.AICharacterEntity;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import ar.edu.utn.frc.tup.piii.repository.jpa.AiCharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AICharacterServiceTest {

    @Mock
    private AiCharacterRepository aiCharacterRepository;

    @InjectMocks
    private AICharacterService aiCharacterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById_found() {
        Long id = 1L;
        AICharacterEntity entity = new AICharacterEntity(
                id,
                "Botta",
                "botta.png",
                AIProfile.TEACHERS,
                "Un capo del c#"
        );

        when(aiCharacterRepository.findByCharacterId(id)).thenReturn(Optional.of(entity));

        AICharacterDto result = aiCharacterService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getCharacterId());
        assertEquals("Botta", result.getName());
        assertEquals("botta.png", result.getImageUrl());
        assertEquals("Un capo del c#", result.getDescription());
        assertEquals(AIProfile.TEACHERS, result.getProfile());

        verify(aiCharacterRepository).findByCharacterId(id);
    }

    @Test
    void testGetById_notFound() {
        Long id = 99L;
        when(aiCharacterRepository.findByCharacterId(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            aiCharacterService.getById(id);
        });

        assertTrue(exception.getMessage().contains("AI Character con el id " + id + " no encontrado."));
        verify(aiCharacterRepository).findByCharacterId(id);
    }

    @Test
    void testGetAll() {
        AICharacterEntity c1 = new AICharacterEntity(1L, "Einstein", "einstein.png", AIProfile.TEACHERS, "Físico");
        AICharacterEntity c2 = new AICharacterEntity(2L, "Obama", "obama.jpg", AIProfile.WORLD_LEADERS, "Ex-presidente");

        when(aiCharacterRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<AICharacterDto> result = aiCharacterService.getAll();

        assertEquals(2, result.size());

        AICharacterDto dto1 = result.get(0);
        assertEquals("Einstein", dto1.getName());
        assertEquals("einstein.png", dto1.getImageUrl());
        assertEquals(AIProfile.TEACHERS, dto1.getProfile());

        AICharacterDto dto2 = result.get(1);
        assertEquals("Obama", dto2.getName());
        assertEquals("obama.jpg", dto2.getImageUrl());
        assertEquals(AIProfile.WORLD_LEADERS, dto2.getProfile());

        verify(aiCharacterRepository).findAll();
    }

    @Test
    void testGetAllByProfile() {
        AIProfile profile = AIProfile.STUDENTS;

        AICharacterEntity student1 = new AICharacterEntity(3L, "Estudiante 1", "student1.png", profile, "Aprendiendo IA");

        when(aiCharacterRepository.getAllByProfile(profile)).thenReturn(List.of(student1));

        List<AICharacterDto> result = aiCharacterService.getAllByProfile(profile);

        assertEquals(1, result.size());
        AICharacterDto dto = result.get(0);
        assertEquals("Estudiante 1", dto.getName());
        assertEquals("student1.png", dto.getImageUrl());
        assertEquals("Aprendiendo IA", dto.getDescription());
        assertEquals(profile, dto.getProfile());

        verify(aiCharacterRepository).getAllByProfile(profile);
    }
}
