package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.SettingsEntity;
import ar.edu.utn.frc.tup.piii.models.Settings;
import ar.edu.utn.frc.tup.piii.repository.jpa.SettingsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsPersistence {

    private final SettingsJpaRepository settingsJpaRepository;
    private final ModelMapper modelMapper;

    public Settings createSettings(Settings settings) {
        SettingsEntity settingsEntity = modelMapper.map(settings, SettingsEntity.class);
        return modelMapper.map(settingsJpaRepository.save(settingsEntity), Settings.class);
    }

    public Settings findBySettingsId(Long id) {
        SettingsEntity settingsEntity = settingsJpaRepository.findById(id).orElse(null);
        return modelMapper.map(settingsEntity, Settings.class);
    }
}
