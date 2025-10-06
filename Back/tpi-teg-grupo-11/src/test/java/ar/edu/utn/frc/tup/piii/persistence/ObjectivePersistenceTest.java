package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.ObjectiveEntity;
import ar.edu.utn.frc.tup.piii.entities.ObjectiveTypeEntity;
import ar.edu.utn.frc.tup.piii.models.Objective;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.repository.jpa.ObjectiveJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectivePersistenceTest {

    private ObjectiveJpaRepository repository;
    private ModelMapper modelMapper;
    private ObjectivePersistence persistence;

    @BeforeEach
    void setup() {
        repository = mock(ObjectiveJpaRepository.class);
        modelMapper = mock(ModelMapper.class);
        persistence = new ObjectivePersistence(repository, modelMapper);
    }

    @Test
    void findAll_withObjectives_returnsMappedList() {
        ObjectiveEntity entity = new ObjectiveEntity();
        Objective model = new Objective();

        when(repository.findAll()).thenReturn(List.of(entity));
        when(modelMapper.map(entity, Objective.class)).thenReturn(model);

        List<Objective> result = persistence.findAll();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findAllByObjectiveType_validType_returnsMappedList() {
        ObjectiveType type = new ObjectiveType();
        ObjectiveTypeEntity typeEntity = new ObjectiveTypeEntity();
        ObjectiveEntity entity = new ObjectiveEntity();
        Objective model = new Objective();

        when(modelMapper.map(type, ObjectiveTypeEntity.class)).thenReturn(typeEntity);
        when(repository.getAllByObjectiveType(typeEntity)).thenReturn(List.of(entity));
        when(modelMapper.map(entity, Objective.class)).thenReturn(model);

        List<Objective> result = persistence.findAllByObjectiveType(type);

        assertEquals(1, result.size());
        verify(repository).getAllByObjectiveType(typeEntity);
    }

    @Test
    void findById_existingId_returnsMappedObjective() {
        Long id = 1L;
        ObjectiveEntity entity = new ObjectiveEntity();
        Objective model = new Objective();

        when(repository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(entity, Objective.class)).thenReturn(model);

        Objective result = persistence.findById(id);

        assertEquals(model, result);
        verify(repository).getReferenceById(id);
    }
}
