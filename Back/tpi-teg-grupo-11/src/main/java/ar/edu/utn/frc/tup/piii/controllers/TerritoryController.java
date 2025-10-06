package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.TerritoryDto;
import ar.edu.utn.frc.tup.piii.services.TerritoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/territories")
@RequiredArgsConstructor
public class TerritoryController {

    private final TerritoryService service;

    @GetMapping("/map/{mapId}")
    public ResponseEntity<List<TerritoryDto>> getAllByMapId(@PathVariable Long mapId){
        List<TerritoryDto> result = service.getAllByGameId(mapId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<TerritoryDto>> getAllByRegionId(@PathVariable Long regionId){
        List<TerritoryDto> result = service.getAllByRegionId(regionId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{territoryId}")
    public ResponseEntity<TerritoryDto> getById(@PathVariable Long territoryId){
        TerritoryDto result = service.getById(territoryId);
        if(Objects.isNull(result)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el territorio");
        }else{
            return ResponseEntity.ok(result);
        }
    }
}
