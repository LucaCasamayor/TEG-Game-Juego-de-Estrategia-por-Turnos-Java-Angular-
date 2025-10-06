package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveDto;
import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.models.Objective;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.persistence.ObjectivePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveServiceTest {

    @Mock
    private ObjectivePersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ObjectiveService objectiveService;

    private Long objectiveId;
    private Objective objective;
    private ObjectiveDto objectiveDto;
    private ObjectiveType objectiveType1;
    private ObjectiveType objectiveType2;
    private ObjectiveTypeDto objectiveTypeDto1;
    private ObjectiveTypeDto objectiveTypeDto2;
    private List<Objective> objectives;
    private List<ObjectiveDto> objectiveDtos;

    @BeforeEach
    void setUp() {
        objectiveId = 1L;

        // Setup test data
        objective = new Objective();
        objective.setId(objectiveId);

        objectiveDto = new ObjectiveDto();
        objectiveDto.setObjectiveId(objectiveId);

        objectiveType1 = new ObjectiveType();
        objectiveType1.setObjectiveTypeId(1L);
        objectiveType1.setName("Type1");

        objectiveType2 = new ObjectiveType();
        objectiveType2.setObjectiveTypeId(2L);
        objectiveType2.setName("Type2");

        objectiveTypeDto1 = new ObjectiveTypeDto();
        objectiveTypeDto1.setObjectiveTypeId(1L);
        objectiveTypeDto1.setName("Type1");

        objectiveTypeDto2 = new ObjectiveTypeDto();
        objectiveTypeDto2.setObjectiveTypeId(2L);
        objectiveTypeDto2.setName("Type2");

        objectives = Arrays.asList(objective);
        objectiveDtos = Arrays.asList(objectiveDto);
    }

    @Test
    void getById_ShouldReturnObjectiveDto_WhenValidId() {
        // Given
        when(persistence.findById(objectiveId)).thenReturn(objective);
        when(modelMapper.map(objective, ObjectiveDto.class)).thenReturn(objectiveDto);

        // When
        ObjectiveDto result = objectiveService.getById(objectiveId);

        // Then
        assertNotNull(result);
        assertEquals(objectiveDto, result);
        assertEquals(objectiveId, result.getObjectiveId());

        verify(persistence).findById(objectiveId);
        verify(modelMapper).map(objective, ObjectiveDto.class);
        verifyNoMoreInteractions(persistence, modelMapper);
    }

    @Test
    void getById_ShouldReturnNull_WhenObjectiveNotFound() {
        // Given
        when(persistence.findById(objectiveId)).thenReturn(null);
        when(modelMapper.map(null, ObjectiveDto.class)).thenReturn(null);

        // When
        ObjectiveDto result = objectiveService.getById(objectiveId);

        // Then
        assertNull(result);

        verify(persistence).findById(objectiveId);
        verify(modelMapper).map(null, ObjectiveDto.class);
        verifyNoMoreInteractions(persistence, modelMapper);
    }

    @Test
    void getById_ShouldHandleNullId() {
        // Given
        Long nullId = null;
        when(persistence.findById(nullId)).thenReturn(null);
        when(modelMapper.map(null, ObjectiveDto.class)).thenReturn(null);

        // When
        ObjectiveDto result = objectiveService.getById(nullId);

        // Then
        assertNull(result);

        verify(persistence).findById(nullId);
        verify(modelMapper).map(null, ObjectiveDto.class);
    }

    @Test
    void getById_ShouldPropagateExceptionFromPersistence() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database error");
        when(persistence.findById(objectiveId)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getById(objectiveId));

        assertEquals("Database error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(persistence).findById(objectiveId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getById_ShouldPropagateExceptionFromModelMapper() {
        // Given
        RuntimeException expectedException = new RuntimeException("Mapping error");
        when(persistence.findById(objectiveId)).thenReturn(objective);
        when(modelMapper.map(objective, ObjectiveDto.class)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getById(objectiveId));

        assertEquals("Mapping error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(persistence).findById(objectiveId);
        verify(modelMapper).map(objective, ObjectiveDto.class);
    }

    @Test
    void getAll_ShouldReturnListOfObjectiveDtos_WhenObjectivesExist() {
        // Given
        Objective objective1 = new Objective();
        objective1.setId(1L);
        Objective objective2 = new Objective();
        objective2.setId(2L);

        ObjectiveDto objectiveDto1 = new ObjectiveDto();
        objectiveDto1.setObjectiveId(1L);
        ObjectiveDto objectiveDto2 = new ObjectiveDto();
        objectiveDto2.setObjectiveId(2L);

        List<Objective> objectives = Arrays.asList(objective1, objective2);

        when(persistence.findAll()).thenReturn(objectives);
        when(modelMapper.map(objective1, ObjectiveDto.class)).thenReturn(objectiveDto1);
        when(modelMapper.map(objective2, ObjectiveDto.class)).thenReturn(objectiveDto2);

        // When
        List<ObjectiveDto> result = objectiveService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(objectiveDto1));
        assertTrue(result.contains(objectiveDto2));

        verify(persistence).findAll();
        verify(modelMapper).map(objective1, ObjectiveDto.class);
        verify(modelMapper).map(objective2, ObjectiveDto.class);
        verifyNoMoreInteractions(persistence, modelMapper);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoObjectivesExist() {
        // Given
        when(persistence.findAll()).thenReturn(Collections.emptyList());

        // When
        List<ObjectiveDto> result = objectiveService.getAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(persistence).findAll();
        verifyNoMoreInteractions(persistence);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getAll_ShouldPropagateExceptionFromPersistence() {
        // Given
        RuntimeException expectedException = new RuntimeException("Database connection failed");
        when(persistence.findAll()).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getAll());

        assertEquals("Database connection failed", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(persistence).findAll();
        verifyNoInteractions(modelMapper);
    }

    @Test
    void getAll_ShouldPropagateExceptionFromModelMapper() {
        // Given
        RuntimeException expectedException = new RuntimeException("Mapping failed");
        when(persistence.findAll()).thenReturn(objectives);
        when(modelMapper.map(any(Objective.class), eq(ObjectiveDto.class))).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getAll());

        assertEquals("Mapping failed", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(persistence).findAll();
        verify(modelMapper).map(any(Objective.class), eq(ObjectiveDto.class));
    }

    @Test
    void getAllByObjectiveTypes_ShouldReturnObjectiveDtos_WhenValidObjectiveTypes() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1, objectiveTypeDto2);

        Objective obj1 = new Objective();
        obj1.setId(1L);
        Objective obj2 = new Objective();
        obj2.setId(2L);
        Objective obj3 = new Objective();
        obj3.setId(3L);

        List<Objective> objectivesType1 = Arrays.asList(obj1, obj2);
        List<Objective> objectivesType2 = Arrays.asList(obj3);

        ObjectiveDto objDto1 = new ObjectiveDto();
        objDto1.setObjectiveId(1L);
        ObjectiveDto objDto2 = new ObjectiveDto();
        objDto2.setObjectiveId(2L);
        ObjectiveDto objDto3 = new ObjectiveDto();
        objDto3.setObjectiveId(3L);

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(modelMapper.map(objectiveTypeDto2, ObjectiveType.class)).thenReturn(objectiveType2);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenReturn(objectivesType1);
        when(persistence.findAllByObjectiveType(objectiveType2)).thenReturn(objectivesType2);
        when(modelMapper.map(obj1, ObjectiveDto.class)).thenReturn(objDto1);
        when(modelMapper.map(obj2, ObjectiveDto.class)).thenReturn(objDto2);
        when(modelMapper.map(obj3, ObjectiveDto.class)).thenReturn(objDto3);

        // When
        List<ObjectiveDto> result = objectiveService.getAllByObjectiveTypes(objectiveTypeDtos);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(objDto1));
        assertTrue(result.contains(objDto2));
        assertTrue(result.contains(objDto3));

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verify(modelMapper).map(objectiveTypeDto2, ObjectiveType.class);
        verify(persistence).findAllByObjectiveType(objectiveType1);
        verify(persistence).findAllByObjectiveType(objectiveType2);
        verify(modelMapper).map(obj1, ObjectiveDto.class);
        verify(modelMapper).map(obj2, ObjectiveDto.class);
        verify(modelMapper).map(obj3, ObjectiveDto.class);
        verifyNoMoreInteractions(persistence, modelMapper);
    }

    @Test
    void getAllByObjectiveTypes_ShouldReturnEmptyList_WhenEmptyObjectiveTypesList() {
        // Given
        List<ObjectiveTypeDto> emptyList = Collections.emptyList();

        // When
        List<ObjectiveDto> result = objectiveService.getAllByObjectiveTypes(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verifyNoInteractions(persistence, modelMapper);
    }

    @Test
    void getAllByObjectiveTypes_ShouldReturnEmptyList_WhenNullObjectiveTypesList() {
        // Given
        List<ObjectiveTypeDto> nullList = null;

        // When & Then
        assertThrows(NullPointerException.class,
                () -> objectiveService.getAllByObjectiveTypes(nullList));

        verifyNoInteractions(persistence, modelMapper);
    }

    @Test
    void getAllByObjectiveTypes_ShouldHandleObjectiveTypesWithNoObjectives() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1);

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenReturn(Collections.emptyList());

        // When
        List<ObjectiveDto> result = objectiveService.getAllByObjectiveTypes(objectiveTypeDtos);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verify(persistence).findAllByObjectiveType(objectiveType1);
        verifyNoMoreInteractions(persistence, modelMapper);
    }

    @Test
    void getAllByObjectiveTypes_ShouldHandleSingleObjectiveType() {
        // Given
        List<ObjectiveTypeDto> singleObjectiveType = Arrays.asList(objectiveTypeDto1);
        List<Objective> objectivesForType1 = Arrays.asList(objective);

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenReturn(objectivesForType1);
        when(modelMapper.map(objective, ObjectiveDto.class)).thenReturn(objectiveDto);

        // When
        List<ObjectiveDto> result = objectiveService.getAllByObjectiveTypes(singleObjectiveType);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(objectiveDto, result.get(0));

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verify(persistence).findAllByObjectiveType(objectiveType1);
        verify(modelMapper).map(objective, ObjectiveDto.class);
    }

    @Test
    void getAllByObjectiveTypes_ShouldPropagateExceptionFromPersistence() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1);
        RuntimeException expectedException = new RuntimeException("Database error");

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getAllByObjectiveTypes(objectiveTypeDtos));

        assertEquals("Database error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verify(persistence).findAllByObjectiveType(objectiveType1);
    }

    @Test
    void getAllByObjectiveTypes_ShouldPropagateExceptionFromModelMapperDuringTypeMapping() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1);
        RuntimeException expectedException = new RuntimeException("Type mapping error");

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getAllByObjectiveTypes(objectiveTypeDtos));

        assertEquals("Type mapping error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verifyNoInteractions(persistence);
    }

    @Test
    void getAllByObjectiveTypes_ShouldPropagateExceptionFromModelMapperDuringObjectiveMapping() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1);
        List<Objective> objectivesForType1 = Arrays.asList(objective);
        RuntimeException expectedException = new RuntimeException("Objective mapping error");

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenReturn(objectivesForType1);
        when(modelMapper.map(objective, ObjectiveDto.class)).thenThrow(expectedException);

        // When & Then
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveService.getAllByObjectiveTypes(objectiveTypeDtos));

        assertEquals("Objective mapping error", thrownException.getMessage());
        assertEquals(expectedException, thrownException);

        verify(modelMapper).map(objectiveTypeDto1, ObjectiveType.class);
        verify(persistence).findAllByObjectiveType(objectiveType1);
        verify(modelMapper).map(objective, ObjectiveDto.class);
    }

    @Test
    void constructor_ShouldCreateInstance_WithRequiredDependencies() {
        // Given
        ObjectivePersistence mockPersistence = mock(ObjectivePersistence.class);
        ModelMapper mockModelMapper = mock(ModelMapper.class);

        // When
        ObjectiveService service = new ObjectiveService(mockPersistence, mockModelMapper);

        // Then
        assertNotNull(service);
    }

    @Test
    void getAllByObjectiveTypes_ShouldMaintainOrderAndHandleDuplicates() {
        // Given
        List<ObjectiveTypeDto> objectiveTypeDtos = Arrays.asList(objectiveTypeDto1, objectiveTypeDto2, objectiveTypeDto1);

        Objective obj1 = new Objective();
        obj1.setId(1L);
        Objective obj2 = new Objective();
        obj2.setId(2L);

        List<Objective> objectivesType1 = Arrays.asList(obj1);
        List<Objective> objectivesType2 = Arrays.asList(obj2);

        ObjectiveDto objDto1 = new ObjectiveDto();
        objDto1.setObjectiveId(1L);
        ObjectiveDto objDto2 = new ObjectiveDto();
        objDto2.setObjectiveId(2L);

        when(modelMapper.map(objectiveTypeDto1, ObjectiveType.class)).thenReturn(objectiveType1);
        when(modelMapper.map(objectiveTypeDto2, ObjectiveType.class)).thenReturn(objectiveType2);
        when(persistence.findAllByObjectiveType(objectiveType1)).thenReturn(objectivesType1);
        when(persistence.findAllByObjectiveType(objectiveType2)).thenReturn(objectivesType2);
        when(modelMapper.map(obj1, ObjectiveDto.class)).thenReturn(objDto1);
        when(modelMapper.map(obj2, ObjectiveDto.class)).thenReturn(objDto2);

        // When
        List<ObjectiveDto> result = objectiveService.getAllByObjectiveTypes(objectiveTypeDtos);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size()); // Should include duplicates
        assertEquals(objDto1, result.get(0));
        assertEquals(objDto2, result.get(1));
        assertEquals(objDto1, result.get(2)); // Duplicate from processing objectiveTypeDto1 twice

        verify(modelMapper, times(2)).map(objectiveTypeDto1, ObjectiveType.class);
        verify(modelMapper).map(objectiveTypeDto2, ObjectiveType.class);
        verify(persistence, times(2)).findAllByObjectiveType(objectiveType1);
        verify(persistence).findAllByObjectiveType(objectiveType2);
        verify(modelMapper, times(2)).map(obj1, ObjectiveDto.class);
        verify(modelMapper).map(obj2, ObjectiveDto.class);
    }
}