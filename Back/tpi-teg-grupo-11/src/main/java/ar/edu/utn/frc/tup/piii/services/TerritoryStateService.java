package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryStateDto;
import ar.edu.utn.frc.tup.piii.entities.TerritoryStateEntity;
import ar.edu.utn.frc.tup.piii.mappers.TerritoryStateMapper;
import ar.edu.utn.frc.tup.piii.models.TerritoryState;
import ar.edu.utn.frc.tup.piii.persistence.TerritoryStatePersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TerritoryStateService{

    private final TerritoryStatePersistence persistence;
    private final ModelMapper modelMapper;
    private final TerritoryStateMapper territoryStateMapper;


    public List<TerritoryStateDto> findByPlayer(Long id) {
        return persistence.findTerritoryStateByPlayer(id)
                .stream()
                .map(ter -> modelMapper.map(ter, TerritoryStateDto.class))
                .collect(Collectors.toList());
    }

    public List<TerritoryState> findAllByRegionId(Long id){
        return persistence.findByRegionId(id);
    }


    public TerritoryStateDto create(TerritoryStateDto ter) {
        TerritoryState model = territoryStateMapper.toModel(ter);
        return modelMapper.map(persistence.createTerritoryState(model), TerritoryStateDto.class);
    }

    public TerritoryStateDto update(Long id, TerritoryStateDto ter) {
        TerritoryState model = territoryStateMapper.toModel(ter);
        return modelMapper.map(persistence.updateTerritoryState(id, model), TerritoryStateDto.class);
    }

//    public void delete(Long id) {
//        persistence.deleteTerritoryState(id);
//    }

    public TerritoryState findByIdModel(Long territoryId) {
        return persistence.findById(territoryId);
    }

    public TerritoryState updateModel(Long territoryStateId, TerritoryState end) {
        return persistence.updateTerritoryState(territoryStateId, end);
    }

    public List<TerritoryState> findByPlayerModel(Long playerId) {
        return persistence.findTerritoryStateByPlayer(playerId);
    }
}
