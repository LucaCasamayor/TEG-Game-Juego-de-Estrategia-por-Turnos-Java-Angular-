import {Game} from './game';
import {TurnPhase} from '../../enums/turn-phase';
import {Player} from './player';
import {Movement} from './movement';

export interface Turn {
  turnId: number,
  game: Game,
  turnPhase: TurnPhase,
  player: Player,
  movements: Movement[]
}
