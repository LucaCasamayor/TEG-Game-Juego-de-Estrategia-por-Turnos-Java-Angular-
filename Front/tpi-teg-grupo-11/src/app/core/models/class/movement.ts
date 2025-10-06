import { TurnPhase } from "../../enums/turn-phase"

export interface Movement {
  movementType: TurnPhase | null  ,
  id?: number,
  startTerritoryId?: number,
  endTerritoryId?: number,
  armyCount?: number

}
