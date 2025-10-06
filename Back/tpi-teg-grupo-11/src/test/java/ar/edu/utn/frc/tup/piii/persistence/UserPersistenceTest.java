package ar.edu.utn.frc.tup.piii.persistence;
import ar.edu.utn.frc.tup.piii.entities.UserEntity;
import ar.edu.utn.frc.tup.piii.models.User;
import ar.edu.utn.frc.tup.piii.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPersistenceTest {

    @Mock
    private UserJpaRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserPersistence persistence;

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        User model = new User();

        when(repository.getReferenceById(id)).thenReturn(entity);
        when(modelMapper.map(entity, User.class)).thenReturn(model);

        User result = persistence.findById(id);

        assertNotNull(result);
        assertEquals(model, result);
        verify(repository).getReferenceById(id);
        verify(modelMapper).map(entity, User.class);
    }

    @Test
    void save() {
        User model = new User();
        UserEntity entity = new UserEntity();
        UserEntity savedEntity = new UserEntity();
        User savedModel = new User();

        when(modelMapper.map(model, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, User.class)).thenReturn(savedModel);

        User result = persistence.save(model);

        assertEquals(savedModel, result);
        verify(repository).save(entity);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        User inputModel = new User();
        inputModel.setUsername("newUsername");
        inputModel.setPassword("newPassword");
        inputModel.setImgUrl("newImgUrl");
        inputModel.setActive(true);

        UserEntity existingEntity = new UserEntity();
        User mappedModel = new User();

        when(repository.getReferenceById(id)).thenReturn(existingEntity);
        when(repository.save(existingEntity)).thenReturn(existingEntity);
        when(modelMapper.map(existingEntity, User.class)).thenReturn(mappedModel);

        User result = persistence.update(id, inputModel);

        assertEquals(mappedModel, result);
        assertEquals("newUsername", existingEntity.getUsername());
        assertEquals("newPassword", existingEntity.getPassword());
        assertEquals("newImgUrl", existingEntity.getImgUrl());
        assertTrue(existingEntity.isActive());

        verify(repository).getReferenceById(id);
        verify(repository).save(existingEntity);
    }

    @Test
    void findByUsernameAndPassword() {
        String username = "user1";
        String password = "pass1";
        UserEntity entity = new UserEntity();
        User model = new User();

        when(repository.findByUsernameAndPassword(username, password)).thenReturn(entity);
        when(modelMapper.map(entity, User.class)).thenReturn(model);

        User result = persistence.findByUsernameAndPassword(username, password);

        assertEquals(model, result);
        verify(repository).findByUsernameAndPassword(username, password);
    }

    @Test
    void testFindByUsername_Found() {
        String username = "user1";
        UserEntity entity = new UserEntity();
        User model = new User();

        when(repository.findByUsername(username)).thenReturn(entity);
        when(modelMapper.map(entity, User.class)).thenReturn(model);

        User result = persistence.findByUsername(username);

        assertEquals(model, result);
    }

    @Test
    void testFindByUsername_NotFound() {
        String username = "unknown";

        when(repository.findByUsername(username)).thenReturn(null);

        User result = persistence.findByUsername(username);

        assertNull(result);
    }
}