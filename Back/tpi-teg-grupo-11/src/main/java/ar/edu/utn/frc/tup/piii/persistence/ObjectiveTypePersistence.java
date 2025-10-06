package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.ObjectiveTypeEntity;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.repository.jpa.ObjectiveTypeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectiveTypePersistence {

    private final ObjectiveTypeJpaRepository repository;
    private final ModelMapper modelMapper;

    public List<ObjectiveType> findAll(){
        List<ObjectiveTypeEntity> objectiveTypeEntities = repository.findAll();
        List<ObjectiveType> objectiveTypes = new ArrayList<>();
        for (ObjectiveTypeEntity ote: objectiveTypeEntities){
            objectiveTypes.add(modelMapper.map(ote, ObjectiveType.class));
        }
        return objectiveTypes;
    }
    public ObjectiveType findById(Long id){
        return modelMapper.map(repository.getReferenceById(id), ObjectiveType.class);
    }

}
