package ar.edu.utn.frc.tup.piii.mappers;

import ar.edu.utn.frc.tup.piii.dtos.SettingsDto;
import ar.edu.utn.frc.tup.piii.entities.SettingsEntity;
import ar.edu.utn.frc.tup.piii.models.Settings;

public interface SettingsMapper {
    Settings toModel(SettingsEntity entity);
    SettingsEntity toEntity(SettingsDto model);
    SettingsDto toDto(Settings model);
}
