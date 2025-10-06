package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveDto;
import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.models.Objective;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.models.Player;
import ar.edu.utn.frc.tup.piii.persistence.ObjectivePersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectiveService {
    private final ObjectivePersistence persistence;
    private final ModelMapper modelMapper;

    public ObjectiveDto getById(Long id){
        return modelMapper.map(persistence.findById(id), ObjectiveDto.class);
    }

    public List<ObjectiveDto> getAll() {
        return persistence.findAll().stream()
                .map(objective -> modelMapper.map(objective, ObjectiveDto.class))
                .collect(Collectors.toList());
    }

    public List<ObjectiveDto> getAllByObjectiveTypes(List<ObjectiveTypeDto> objectiveTypes) {
        List<Objective> objectives = new ArrayList<>();
        for (ObjectiveTypeDto ot : objectiveTypes){
            objectives.addAll(persistence.findAllByObjectiveType(modelMapper.map(ot, ObjectiveType.class)));
        }
        return objectives.stream()
                .map(objective -> modelMapper.map(objective, ObjectiveDto.class))
                .collect(Collectors.toList());
    }
}
