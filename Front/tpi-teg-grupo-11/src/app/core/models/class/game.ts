import {Settings} from './settings';
import {GameState} from '../../enums/game-state';
import {Player} from './player';

export interface Game {
  gameId?: string,
  settings: Settings,
  gameState: GameState,
  creationDate: Date,
  players: Player[],
}
