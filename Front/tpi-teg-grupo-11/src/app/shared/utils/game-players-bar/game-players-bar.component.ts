import {Component, Input, type OnInit} from "@angular/core"
import type {Player} from "../../../core/models/class/player"
import {PlayerCardComponent} from '../player-card/player-card.component';

@Component({
  selector: "app-game-players-bar",
  templateUrl: "./game-players-bar.component.html",
  styleUrls: ["./game-players-bar.component.css"],
  imports: [
    PlayerCardComponent
  ]
})
export class GamePlayersBarComponent implements OnInit {
  @Input() players: Player[] = []
  @Input() currentPlayer: Player | undefined;

  sortedPlayers: Player[] = []

  ngOnInit() {
    this.sortPlayersByTurnOrder()
  }

  // ngOnChanges() {
  //   this.sortPlayersByTurnOrder()
  // }

  private sortPlayersByTurnOrder() {
    this.sortedPlayers = [...this.players].sort((a, b) => {
      if (a.hasLost !== b.hasLost) {
        return a.hasLost ? 1 : -1
      }
      return a.turnOrder - b.turnOrder
    })
  }

  getCurrentPlayer() {
    if(this.currentPlayer) {
      return this.currentPlayer;
    } else {
      return null;
    }
  }

  getActivePlayers(): number {
    return this.players.filter((player) => !player.hasLost).length
  }

  getEliminatedPlayers(): number {
    return this.players.filter((player) => player.hasLost).length
  }
}
