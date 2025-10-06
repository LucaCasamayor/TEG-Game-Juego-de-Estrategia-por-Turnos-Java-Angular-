import {User} from './user';
import {Objective} from './objective';
import {Color} from '../../enums/color';
import {PlayerType} from '../../enums/player-type';
import {TerritoryState} from './game-data';

export interface Player {
  playerId?: number;
  user?: User;
  gameId: string;
  objective?: Objective;
  playerColor?: Color;
  turnOrder: number;
  isWinner: boolean;
  hasLost: boolean;
  difficulty?: string;
  playerType: PlayerType;
  territories?: TerritoryState[];
}
