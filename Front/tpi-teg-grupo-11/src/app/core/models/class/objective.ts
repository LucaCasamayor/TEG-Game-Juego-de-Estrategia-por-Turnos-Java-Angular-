import {ObjectiveType} from './objective-type';
import {Color} from '../../enums/color';

export interface Objective {
  objectiveId: number;
  description: string;
  objectiveType: ObjectiveType;
  color: Color;
  firstRegionTerritoriesNeeded: number;
  secondRegionTerritoriesNeeded: number;
  thirdRegionTerritoriesNeeded: number;
  fourthRegionTerritoriesNeeded: number;
  fifthRegionTerritoriesNeeded: number;
}
