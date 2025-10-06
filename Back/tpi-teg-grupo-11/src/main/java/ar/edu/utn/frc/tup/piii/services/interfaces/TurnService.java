package ar.edu.utn.frc.tup.piii.services.interfaces;


import ar.edu.utn.frc.tup.piii.models.Turn;

public interface TurnService {
    Turn playDeployStage(Turn turn) throws Exception;
    Turn playAttackStage(Turn turn) throws Exception;
    Turn playFortifyStage(Turn turn)throws Exception;
    Turn playCardStage(Turn turn);

}

