package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TurnBotMediumImpl implements TurnService {


    @Override
    public Turn playDeployStage(Turn turn) {
//        TODO
//        Lograr que el Bot de media dificultad
//        juegue su fase de DESPLIEGUE según la estrategia
//        propuesta en el enunciado del TPI
//        Algunos puntos a tener en cuenta:
//          - Si puede atacar un país con al menos el doble de tropas que el enemigo,
//          ataca.
//          - Si hay varios países atacables, prioriza los que sean parte de su objetivo.
//          - Si no tiene ataques favorables, pasa sin atacar.
//          - Mueve tropas a los territorios conquistados, pero deja al menos 2 tropas
//          de reserva en el original.
        return null;
    }

    @Override
    public Turn playAttackStage(Turn turn) {
//        TODO
        return  null;
//        Lograr que el Bot de media dificultad
//        juegue su fase de ATAQUE según la estrategia
//        propuesta en el enunciado del TPI.
//        Reglas:
//          - Refuerza primero los territorios fronterizos más expuestos, los que tienen
//          menos tropas que sus vecinos.
//          - Si tiene tropas extra, las coloca en países clave para cumplir su misión.
    }

    @Override
    public Turn playFortifyStage(Turn turn) throws Exception {
//        TODO
//        Lograr que el Bot de media dificultad
//        juegue su fase de FORTIFICACIÓN según la estrategia
//        propuesta en el enunciado del TPI
//        Reglas:
//          - Mueve tropas hacia la frontera si tiene un bloque seguro de territorios.
//          - Si un territorio aislado tiene tropas en exceso, mueve tropas hacia otro
//          territorio propio
        return null;
    }
    @Override
    public Turn playCardStage(Turn turn) {
//        TODO
//        Lograr que el Bot de media dificultad
//        juegue su fase de CARTAS
        return null;
    }
}
