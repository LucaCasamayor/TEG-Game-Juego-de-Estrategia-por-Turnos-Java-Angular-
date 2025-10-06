import {Player} from './player';
import {Turn} from './turn';
import {GameState} from '../../enums/game-state';
import {GameMap} from './game-map';

export interface Region {
  id: number;
  name: string;
  numberTerritories: number;
  map: GameMap;
}

export interface Territory {
  territoryId: number;
  name: string;
  bordersId: number[];
  region: Region;
}

export interface TerritoryState {
  territoryStateId: number;
  turn: Turn;
  territory: Territory;
  player: Player;
  armyCount: number;
  owner?: Player
}

export interface GameData {
  id: string;
  territories: Territory[];
  players: Player[];
  gameState: GameState;
  currentTurnIndex: number;
  currentTurn: Turn;
  turns : Turn[];
}

