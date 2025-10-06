package ar.edu.utn.frc.tup.piii.services.implementations;

import ar.edu.utn.frc.tup.piii.models.Turn;
import ar.edu.utn.frc.tup.piii.services.interfaces.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("turnBotHardImpl")
@RequiredArgsConstructor
public class TurnBotHardImpl implements TurnService {

    @Override
    public Turn playDeployStage(Turn turn) {
//        TODO
//        Lograr que el Bot de alta dificultad
//        juegue su fase de DESPLIEGUE según la estrategia
//        propuesta en el enunciado del TPI
//        Algunos puntos a tener en cuenta:
//          - Si puede atacar un país con al menos el doble de tropas que el enemigo,
//          ataca.
//          - Refuerza territorios clave para su misión sin descuidar defensas.
//          - Si detecta que un jugador está cerca de ganar, coloca tropas para
//          bloquearlo.
        return null;
    }

    @Override
    public Turn playAttackStage(Turn turn) {
//        TODO
//        Lograr que el Bot de alta dificultad
//        juegue su fase de ATAQUE según la estrategia
//        propuesta en el enunciado del TPI.
//        Reglas:
//          - Evalúa probabilidad de éxito (ejemplo: solo ataca si tiene al menos 3 veces
//          las tropas del enemigo).
//          - Prioriza atacar territorios que lo acerquen a cumplir su misión.
//          - Si no hay ataques estratégicamente beneficiosos, no ataca y refuerza en
//          su turno.
        return null;
    }

    @Override
    public Turn playFortifyStage(Turn turn) {
//        TODO
//        Lograr que el Bot de alta dificultad
//        juegue su fase de FORTIFICACIÓN según la estrategia
//        propuesta en el enunciado del TPI
//        Reglas:
//          - Reagrupa tropas para consolidar defensas y abrir caminos para futuros
//          ataques.
//          - Si detecta que otro jugador está intentando ganar, redistribuye tropas para
//          evitarlo.
        return null;
    }

    @Override
    public Turn playCardStage(Turn turn) {
        return null;
    }

}
