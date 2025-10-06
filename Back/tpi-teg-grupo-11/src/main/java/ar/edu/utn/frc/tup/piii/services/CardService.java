package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.CardDto;
import ar.edu.utn.frc.tup.piii.persistence.CardPersistence;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardPersistence persistence;
    private final ModelMapper modelMapper;

    public List<CardDto> getAll() {
        return persistence.findAllCards()
                .stream()
                .map(card -> modelMapper.map(card, CardDto.class))
                .collect(Collectors.toList());
    }

    public CardDto getById(Long id) {
        return modelMapper.map(persistence.findCardById(id), CardDto.class);
    }
}

