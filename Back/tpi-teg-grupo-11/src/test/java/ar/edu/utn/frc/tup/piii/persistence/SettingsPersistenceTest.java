package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.SettingsEntity;
import ar.edu.utn.frc.tup.piii.models.Settings;
import ar.edu.utn.frc.tup.piii.repository.jpa.SettingsJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingsPersistenceTest {

    private SettingsJpaRepository settingsJpaRepository;
    private ModelMapper modelMapper;
    private SettingsPersistence settingsPersistence;

    @BeforeEach
    void setup() {
        settingsJpaRepository = mock(SettingsJpaRepository.class);
        modelMapper = mock(ModelMapper.class);
        settingsPersistence = new SettingsPersistence(settingsJpaRepository, modelMapper);
    }

    @Test
    void createSettings_validSettings_returnsSavedSettings() {
        Settings settings = new Settings();
        SettingsEntity entity = new SettingsEntity();

        when(modelMapper.map(settings, SettingsEntity.class)).thenReturn(entity);
        when(settingsJpaRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, Settings.class)).thenReturn(settings);

        Settings result = settingsPersistence.createSettings(settings);

        assertNotNull(result);
        verify(settingsJpaRepository).save(entity);
    }

    @Test
    void findBySettingsId_existingId_returnsSettings() {
        Long id = 1L;
        SettingsEntity entity = new SettingsEntity();
        Settings settings = new Settings();

        when(settingsJpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, Settings.class)).thenReturn(settings);

        Settings result = settingsPersistence.findBySettingsId(id);

        assertNotNull(result);
        verify(settingsJpaRepository).findById(id);
    }

    @Test
    void findBySettingsId_nonExistingId_returnsNull() {
        Long id = 99L;

        when(settingsJpaRepository.findById(id)).thenReturn(Optional.empty());

        Settings result = settingsPersistence.findBySettingsId(id);

        assertNull(result);
        verify(settingsJpaRepository).findById(id);
    }
}
