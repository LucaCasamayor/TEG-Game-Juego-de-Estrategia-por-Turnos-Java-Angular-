package ar.edu.utn.frc.tup.piii.persistence;

import ar.edu.utn.frc.tup.piii.models.Region;
import ar.edu.utn.frc.tup.piii.repository.jpa.RegionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionPersistence {

    private final RegionJpaRepository repository;
    private final ModelMapper modelMapper;

    public Region findById(Long id) {
        return modelMapper.map(repository.getReferenceById(id), Region.class);
    }
    public List<Region> findByMapId(Long mapId){
        return repository.getAllByMap_GameMapId(mapId).stream()
                .map(entity -> modelMapper.map(entity, Region.class))
                .collect(Collectors.toList());
    }
}