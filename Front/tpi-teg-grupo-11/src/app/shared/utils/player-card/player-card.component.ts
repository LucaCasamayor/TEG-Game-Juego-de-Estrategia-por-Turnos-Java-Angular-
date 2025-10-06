import {Component, Input} from '@angular/core';

import {Color} from "../../../core/enums/color"

import {Player} from "../../../core/models/class/player"
import {NgClass} from '@angular/common';
import {PlayerType} from '../../../core/enums/player-type';

@Component({
  selector: 'app-player-card',
  templateUrl: './player-card.component.html',
  imports: [
    NgClass
  ],
  styleUrls: ['./player-card.component.css']
})
export class PlayerCardComponent {
  @Input() player!: Player;

  getColorClass(): string {
    switch (this.player.playerColor) {
      case Color.RED:
        return 'bg-red-500';
      case Color.BLUE:
        return 'bg-sky-800';
      case Color.GREEN:
        return 'bg-green-500';
      case Color.YELLOW:
        return 'bg-yellow-500';
      case Color.ORANGE:
        return 'bg-orange-500';
      case Color.PURPLE:
        return 'bg-purple-500';
      default:
        return 'bg-gray-500';
    }
  }

  getCardClasses(): string {
    let classes = 'player-card';

    if (this.player.turnOrder == 0) {
      classes += ' current-turn';
    }

    if (this.player.hasLost) {
      classes += ' eliminated';
    }

    classes += ` ${this.getColorClass()}`;

    return classes;
  }

  protected readonly PlayerType = PlayerType;
}
