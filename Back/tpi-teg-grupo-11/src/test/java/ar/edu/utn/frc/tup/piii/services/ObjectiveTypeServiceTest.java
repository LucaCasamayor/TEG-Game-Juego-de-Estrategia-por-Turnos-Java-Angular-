package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.persistence.ObjectiveTypePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectiveTypeServiceTest {

    @Mock
    private ObjectiveTypePersistence persistence;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ObjectiveTypeService objectiveTypeService;

    private ObjectiveType mockObjectiveType;
    private ObjectiveTypeDto mockObjectiveTypeDto;

    @BeforeEach
    void setUp() {
        mockObjectiveType = new ObjectiveType();
        mockObjectiveType.setObjectiveTypeId(1L);

        mockObjectiveTypeDto = new ObjectiveTypeDto();
        mockObjectiveTypeDto.setObjectiveTypeId(1L);

    }

    @Test
    void getById_ValidId_ReturnsObjectiveTypeDto() {
        Long validId = 1L;
        when(persistence.findById(validId)).thenReturn(mockObjectiveType);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenReturn(mockObjectiveTypeDto);

        ObjectiveTypeDto result = objectiveTypeService.getById(validId);

        assertNotNull(result);
        assertEquals(mockObjectiveTypeDto.getObjectiveTypeId(), result.getObjectiveTypeId());
        assertEquals(mockObjectiveTypeDto.getName(), result.getName());

        verify(persistence, times(1)).findById(validId);
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
    }

    @Test
    void getById_NullId_CallsPersistenceWithNull() {
        Long nullId = null;
        when(persistence.findById(nullId)).thenReturn(mockObjectiveType);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenReturn(mockObjectiveTypeDto);

        ObjectiveTypeDto result = objectiveTypeService.getById(nullId);

        assertNotNull(result);
        verify(persistence, times(1)).findById(nullId);
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
    }

    @Test
    void getById_PersistenceReturnsNull_MapsNull() {
        Long validId = 1L;
        when(persistence.findById(validId)).thenReturn(null);
        when(modelMapper.map(null, ObjectiveTypeDto.class)).thenReturn(null);

        ObjectiveTypeDto result = objectiveTypeService.getById(validId);

        assertNull(result);
        verify(persistence, times(1)).findById(validId);
        verify(modelMapper, times(1)).map(null, ObjectiveTypeDto.class);
    }

    @Test
    void getById_PersistenceThrowsException_PropagatesException() {
        Long validId = 1L;
        RuntimeException expectedException = new RuntimeException("Database error");
        when(persistence.findById(validId)).thenThrow(expectedException);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveTypeService.getById(validId));

        assertEquals("Database error", thrownException.getMessage());
        verify(persistence, times(1)).findById(validId);
        verify(modelMapper, never()).map(any(), eq(ObjectiveTypeDto.class));
    }

    @Test
    void getById_ModelMapperThrowsException_PropagatesException() {
        Long validId = 1L;
        RuntimeException expectedException = new RuntimeException("Mapping error");
        when(persistence.findById(validId)).thenReturn(mockObjectiveType);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenThrow(expectedException);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveTypeService.getById(validId));

        assertEquals("Mapping error", thrownException.getMessage());
        verify(persistence, times(1)).findById(validId);
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
    }

    @Test
    void getAll_WithData_ReturnsListOfObjectiveTypeDto() {
        ObjectiveType objectiveType2 = new ObjectiveType();
        objectiveType2.setObjectiveTypeId(2L);
        objectiveType2.setName("Second Objective Type");

        ObjectiveTypeDto objectiveTypeDto2 = new ObjectiveTypeDto();
        objectiveTypeDto2.setObjectiveTypeId(2L);
        objectiveTypeDto2.setName("Second Objective Type");

        List<ObjectiveType> mockObjectiveTypes = Arrays.asList(mockObjectiveType, objectiveType2);

        when(persistence.findAll()).thenReturn(mockObjectiveTypes);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenReturn(mockObjectiveTypeDto);
        when(modelMapper.map(objectiveType2, ObjectiveTypeDto.class)).thenReturn(objectiveTypeDto2);

        List<ObjectiveTypeDto> result = objectiveTypeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockObjectiveTypeDto.getObjectiveTypeId(), result.get(0).getObjectiveTypeId());
        assertEquals(objectiveTypeDto2.getObjectiveTypeId(), result.get(1).getObjectiveTypeId());

        verify(persistence, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(ObjectiveType.class), eq(ObjectiveTypeDto.class));
    }

    @Test
    void getAll_NoData_ReturnsEmptyList() {
        when(persistence.findAll()).thenReturn(Collections.emptyList());

        List<ObjectiveTypeDto> result = objectiveTypeService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());

        verify(persistence, times(1)).findAll();
        verify(modelMapper, never()).map(any(ObjectiveType.class), eq(ObjectiveTypeDto.class));
    }

    @Test
    void getAll_SingleItem_ReturnsSingleItemList() {
        List<ObjectiveType> singleItemList = Collections.singletonList(mockObjectiveType);
        when(persistence.findAll()).thenReturn(singleItemList);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenReturn(mockObjectiveTypeDto);

        List<ObjectiveTypeDto> result = objectiveTypeService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockObjectiveTypeDto.getObjectiveTypeId(), result.get(0).getObjectiveTypeId());

        verify(persistence, times(1)).findAll();
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
    }

    @Test
    void getAll_PersistenceThrowsException_PropagatesException() {
        RuntimeException expectedException = new RuntimeException("Database connection failed");
        when(persistence.findAll()).thenThrow(expectedException);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveTypeService.getAll());

        assertEquals("Database connection failed", thrownException.getMessage());
        verify(persistence, times(1)).findAll();
        verify(modelMapper, never()).map(any(ObjectiveType.class), eq(ObjectiveTypeDto.class));
    }

    @Test
    void getAll_ModelMapperThrowsException_PropagatesException() {
        List<ObjectiveType> mockObjectiveTypes = Collections.singletonList(mockObjectiveType);
        RuntimeException expectedException = new RuntimeException("Mapping failed");

        when(persistence.findAll()).thenReturn(mockObjectiveTypes);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenThrow(expectedException);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> objectiveTypeService.getAll());

        assertEquals("Mapping failed", thrownException.getMessage());
        verify(persistence, times(1)).findAll();
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
    }

    @Test
    void getAll_ListContainsNull_HandlesNullObjects() {
        List<ObjectiveType> listWithNull = Arrays.asList(mockObjectiveType, null);
        when(persistence.findAll()).thenReturn(listWithNull);
        when(modelMapper.map(mockObjectiveType, ObjectiveTypeDto.class)).thenReturn(mockObjectiveTypeDto);
        when(modelMapper.map(null, ObjectiveTypeDto.class)).thenReturn(null);

        List<ObjectiveTypeDto> result = objectiveTypeService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockObjectiveTypeDto.getObjectiveTypeId(), result.get(0).getObjectiveTypeId());
        assertNull(result.get(1));

        verify(persistence, times(1)).findAll();
        verify(modelMapper, times(1)).map(mockObjectiveType, ObjectiveTypeDto.class);
        verify(modelMapper, times(1)).map(null, ObjectiveTypeDto.class);
    }

    @Test
    void constructor_WithValidDependencies_CreatesService() {
        ObjectiveTypeService service = new ObjectiveTypeService(persistence, modelMapper);

        assertNotNull(service);
    }

    @Test
    void getAll_MultipleItems_PreservesOrder() {
        ObjectiveType first = createObjectiveType(1L, "First");
        ObjectiveType second = createObjectiveType(2L, "Second");
        ObjectiveType third = createObjectiveType(3L, "Third");

        ObjectiveTypeDto firstDto = createObjectiveTypeDto(1L, "First");
        ObjectiveTypeDto secondDto = createObjectiveTypeDto(2L, "Second");
        ObjectiveTypeDto thirdDto = createObjectiveTypeDto(3L, "Third");

        List<ObjectiveType> orderedList = Arrays.asList(first, second, third);

        when(persistence.findAll()).thenReturn(orderedList);
        when(modelMapper.map(first, ObjectiveTypeDto.class)).thenReturn(firstDto);
        when(modelMapper.map(second, ObjectiveTypeDto.class)).thenReturn(secondDto);
        when(modelMapper.map(third, ObjectiveTypeDto.class)).thenReturn(thirdDto);

        List<ObjectiveTypeDto> result = objectiveTypeService.getAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("First", result.get(0).getName());
        assertEquals("Second", result.get(1).getName());
        assertEquals("Third", result.get(2).getName());

        verify(persistence, times(1)).findAll();
        verify(modelMapper, times(3)).map(any(ObjectiveType.class), eq(ObjectiveTypeDto.class));
    }

    private ObjectiveType createObjectiveType(Long id, String name) {
        ObjectiveType obj = new ObjectiveType();
        obj.setObjectiveTypeId(id);
        obj.setName(name);
        return obj;
    }

    private ObjectiveTypeDto createObjectiveTypeDto(Long id, String name) {
        ObjectiveTypeDto dto = new ObjectiveTypeDto();
        dto.setObjectiveTypeId(id);
        dto.setName(name);
        return dto;
    }
}