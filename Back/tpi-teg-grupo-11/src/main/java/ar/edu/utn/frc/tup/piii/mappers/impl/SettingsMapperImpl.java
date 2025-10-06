package ar.edu.utn.frc.tup.piii.mappers.impl;

import ar.edu.utn.frc.tup.piii.dtos.SettingsDto;
import ar.edu.utn.frc.tup.piii.entities.SettingsEntity;
import ar.edu.utn.frc.tup.piii.mappers.SettingsMapper;
import ar.edu.utn.frc.tup.piii.models.GameMap;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.models.Settings;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SettingsMapperImpl implements SettingsMapper {

    private final ModelMapper modelMapper;

    @Override
    public Settings toModel(SettingsEntity entity) {
        if (entity == null) {
            return null;
        }

        Settings settings = new Settings();
        settings.setSettingsId(entity.getSettingsId());
        settings.setTurnTime(entity.getTurnTime());
        settings.setAiProfile(entity.getAiProfile());
        settings.setPassword(entity.getPassword());
        settings.setPrivate(entity.isPrivate());

        if (entity.getMap() != null) {
            settings.setMap(modelMapper.map(entity.getMap(), GameMap.class));
        }

        settings.setObjectiveTypes(
                entity.getObjectiveTypes().stream()
                        .map(x -> modelMapper.map(x, ObjectiveType.class))
                        .collect(Collectors.toList())
        );

        return settings;
    }

    @Override
    public SettingsEntity toEntity(SettingsDto model) {
        if (model == null) {
            return null;
        }

        SettingsEntity entity = new SettingsEntity();
        entity.setSettingsId(model.getSettingsId());
        entity.setTurnTime(model.getTurnTime());
        entity.setAiProfile(model.getAiProfile());
        entity.setPassword(model.getPassword());
        entity.setPrivate(model.isPrivate());

        // NO mapear relaciones aca - se manejan en el servicio

        return entity;
    }

    @Override
    public SettingsDto toDto(Settings model) {
        return null;
    }
}
