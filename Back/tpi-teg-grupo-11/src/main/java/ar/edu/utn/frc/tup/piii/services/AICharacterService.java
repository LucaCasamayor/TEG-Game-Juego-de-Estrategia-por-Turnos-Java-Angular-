package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.AICharacterDto;
import ar.edu.utn.frc.tup.piii.entities.AICharacterEntity;
import ar.edu.utn.frc.tup.piii.enums.AIProfile;
import ar.edu.utn.frc.tup.piii.repository.jpa.AiCharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AICharacterService {

    private final AiCharacterRepository aiCharacterRepository;

    public AICharacterService(AiCharacterRepository aiCharacterRepository) {
        this.aiCharacterRepository = aiCharacterRepository;
    }

    public AICharacterDto getById(Long id) {
        AICharacterEntity entity = aiCharacterRepository.findByCharacterId(id)
                .orElseThrow(() -> new EntityNotFoundException("AI Character con el id " + id + " no encontrado."));
        return toDto(entity);
    }

    public List<AICharacterDto> getAll() {
        return aiCharacterRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<AICharacterDto> getAllByProfile(AIProfile profile) {
        return aiCharacterRepository.getAllByProfile(profile).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AICharacterDto toDto(AICharacterEntity entity) {
        AICharacterDto dto = new AICharacterDto();
        dto.setCharacterId(entity.getCharacterId());
        dto.setName(entity.getName());
        dto.setImageUrl(entity.getImage());
        dto.setProfile(entity.getProfile());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
