import {GameState} from './game-state';

export interface SavedGame {
  id: number,
  code: string,
  gameState: GameState
}
