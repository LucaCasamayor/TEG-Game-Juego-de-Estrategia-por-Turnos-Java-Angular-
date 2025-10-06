package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.SettingsDto;
import ar.edu.utn.frc.tup.piii.models.Settings;
import ar.edu.utn.frc.tup.piii.persistence.SettingsPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final SettingsPersistence persistence;
    private final ModelMapper modelMapper;


    public SettingsDto findById(Long id) {
        return modelMapper.map(persistence.findBySettingsId(id), SettingsDto.class);
    }

    public SettingsDto createSettings(SettingsDto settings) {
        Settings result = persistence.createSettings(modelMapper.map(settings, Settings.class));
        return modelMapper.map(result, SettingsDto.class);
    }
}
