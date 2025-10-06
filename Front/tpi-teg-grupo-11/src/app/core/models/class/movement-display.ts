import {TurnPhase} from "../../enums/turn-phase";

export interface MovementDisplay {
  id?: number,
  startTerritoryId?: number,
  endTerritoryId?: number,
  armyCount?: number
  movementType : TurnPhase,
  dice? : Dice[]
}
export interface Dice {
  diceId: number;
  attackerDice: number;
  defenderDice: number;
}
