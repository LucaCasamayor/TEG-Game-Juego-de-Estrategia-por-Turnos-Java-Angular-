package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ObjectiveTypeDto;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.persistence.ObjectiveTypePersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectiveTypeService {

    private final ObjectiveTypePersistence persistence;
    private final ModelMapper modelMapper;


    public ObjectiveTypeDto getById(Long id) {
        ObjectiveType obj = persistence.findById(id);
        return modelMapper.map(obj, ObjectiveTypeDto.class);

    }

    public List<ObjectiveTypeDto> getAll() {
        return persistence.findAll().
                stream().map(ObjectiveType -> modelMapper.map(ObjectiveType, ObjectiveTypeDto.class)).collect(Collectors.toList());
    }

}
