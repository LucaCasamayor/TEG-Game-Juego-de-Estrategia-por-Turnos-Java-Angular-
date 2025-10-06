package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.ObjectiveTypeEntity;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.repository.jpa.ObjectiveTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectiveTypePersistenceTest {

    private ObjectiveTypeJpaRepository repository;
    private ModelMapper modelMapper;
    private ObjectiveTypePersistence persistence;

    @BeforeEach
    void setUp() {
        repository = mock(ObjectiveTypeJpaRepository.class);
        modelMapper = mock(ModelMapper.class);
        persistence = new ObjectiveTypePersistence(repository, modelMapper);
    }

    @Test
    void findAll_returnsMappedObjectiveTypeList() {
        ObjectiveTypeEntity entity = new ObjectiveTypeEntity();
        ObjectiveType model = new ObjectiveType();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(modelMapper.map(entity, ObjectiveType.class)).thenReturn(model);

        List<ObjectiveType> result = persistence.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(modelMapper).map(entity, ObjectiveType.class);
    }

    @Test
    void findById_existingId_returnsMappedObjectiveType() {
        Long id = 1L;
        ObjectiveTypeEntity entity = new ObjectiveTypeEntity();
        ObjectiveType model = new ObjectiveType();

        when(repository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(entity, ObjectiveType.class)).thenReturn(model);

        ObjectiveType result = persistence.findById(id);

        assertEquals(model, result);
        verify(repository).getReferenceById(id);
    }
}
