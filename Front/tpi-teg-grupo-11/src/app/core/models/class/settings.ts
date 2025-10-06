import {GameMap} from './game-map';
import {AIProfile} from '../../enums/ai-profile';
import {ObjectiveType} from './objective-type';

export interface Settings {
  settingsId?: number,
  map: GameMap,
  objectiveTypes: ObjectiveType[],
  turnTime: number,
  aiProfile: AIProfile,
  isPrivate: boolean,
  password?: string,
}
