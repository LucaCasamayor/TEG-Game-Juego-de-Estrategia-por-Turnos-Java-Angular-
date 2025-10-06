package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.entities.ObjectiveEntity;
import ar.edu.utn.frc.tup.piii.entities.ObjectiveTypeEntity;
import ar.edu.utn.frc.tup.piii.models.Objective;
import ar.edu.utn.frc.tup.piii.models.ObjectiveType;
import ar.edu.utn.frc.tup.piii.repository.jpa.ObjectiveJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectivePersistence {
    private final ObjectiveJpaRepository repository;
    private final ModelMapper modelMapper;

    public List<Objective> findAll() {
        List<ObjectiveEntity> objectiveEntities = repository.findAll();
        List<Objective> objectives = new ArrayList<>();
        for (ObjectiveEntity oe : objectiveEntities) {
            objectives.add(modelMapper.map(oe, Objective.class));
        }
        return objectives;
    }

    public List<Objective> findAllByObjectiveType(ObjectiveType objectiveType) {
        ObjectiveTypeEntity objectiveTypeEntity = modelMapper.map(objectiveType, ObjectiveTypeEntity.class);
        List<ObjectiveEntity> objectiveEntities = repository.getAllByObjectiveType(objectiveTypeEntity);
        List<Objective> objectives = new ArrayList<>();
        for (ObjectiveEntity oe : objectiveEntities) {
            objectives.add(modelMapper.map(oe, Objective.class));
        }
        return objectives;
    }
    public Objective findById(Long id){
        return modelMapper.map(repository.getReferenceById(id), Objective.class);
    }


}
