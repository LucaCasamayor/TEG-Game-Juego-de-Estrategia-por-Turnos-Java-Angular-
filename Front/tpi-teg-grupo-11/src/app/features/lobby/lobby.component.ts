import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {NgClass, NgStyle} from '@angular/common';
import {MatIcon} from '@angular/material/icon';
import {PlayerService} from '../../services/players/player.service';
import {Subscription} from 'rxjs';
import {Player} from '../../core/models/class/player';
import {SessionDirective} from '../../shared/directives/session/session.directive';
import {User} from '../../core/models/class/user';
import {BotDifficulty} from '../../core/enums/bot-difficulty';
import {PlayerType} from '../../core/enums/player-type';
import {GameService} from '../../services/game/game.service';
import {Color} from '../../core/enums/color';
import {Game} from '../../core/models/class/game';
import {GameState} from '../../core/enums/game-state';

@Component({
  selector: 'app-lobby',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    MatIcon,
    NgStyle,
    SessionDirective,
    NgClass
  ],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.css'
})
export class LobbyComponent implements OnInit, OnDestroy {

  private subscription = new Subscription();

  gameId!: string;
  session : User | undefined;
  game: Game | undefined;

  constructor(private route: ActivatedRoute,
              private gameService: GameService,
              private playerService : PlayerService,
              private router: Router) {}

  players : Player[] = [];
  botDifficultyEntries = Object.entries(BotDifficulty);
  botSelected = {
    "difficult": "EASY"
  }

  handleUser(user: User) {
    this.session = user;
    this.players = [
      {
        user: this.session,
        turnOrder: 0,
        isWinner: false,
        hasLost: false,
        playerType: PlayerType.PLAYER,
        gameId: this.gameId!,
      }
    ];
  }

  usedColors: Color[] = [];

  /*getRandomColor(): Color | null {
    const allColors = Object.values(Color) as Color[];
    const availableColors = allColors.filter(color => !this.usedColors.includes(color));

    if (availableColors.length === 0) {
      return null;
    }

    const randomIndex = Math.floor(Math.random() * availableColors.length);
    const selectedColor = availableColors[randomIndex];
    this.usedColors.push(selectedColor);
    return selectedColor;
  }
*/

  addBot(difficulty: string) {
    if (this.players.length >= 6) {
      alert("Máximo de jugadores alcanzado");
      return;
    }

    const botBody: Player = {
      playerType: PlayerType.BOT,
      turnOrder: this.players.length,
      isWinner: false,
      hasLost: false,
      difficulty: difficulty,
      gameId: this.gameId!,
    };

    console.log(JSON.stringify(botBody));

    this.subscription = this.playerService.createBotPlayer(botBody).subscribe({
      next: player => {
        alert("Bot creado");
        this.players.push(player);
      },
      error: err => {
        console.log(err);
        alert(err.error.message);
      }
    });
  }

  updateGame(gameId: string, game: Game) {

    game.players = this.players;

    this.subscription = this.gameService.updateGame(gameId, game).subscribe({
      next: game => {
        this.game = game;
        alert(`Jugadores actualizados ${game.players.length}`);
        console.log(this.game);
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  updateGameState() {
    this.subscription = this.gameService.updateGameState(this.gameId, GameState.WAITING_PLAYERS).subscribe({
      next: game => {
        this.game = game;
        console.log(this.game);
        this.router.navigate(['/game', this.gameId]);
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  getGameById(gameId: string) {
    this.gameService.getGameById(this.gameId).subscribe({
      next: game => {
        if(game.gameState === GameState.IN_PROGRESS) {
          this.router.navigate(['/game', this.gameId]);
        } else {
          this.game = game
          this.players = game.players;
          console.log(game);
        }
      },
      error: err => {
        alert(err.error.message);
        console.log("Error al obtener el juego");
        this.router.navigate(['/']);
      }
    });
  }

  initGame(players: Player[]) {
    console.log("initGame ejecuted")
    if (players.length < 3) {
      alert("Mínimo 3 jugadores");
    } else if (!this.gameId || !this.game) {
      alert("El juego todavía no está cargado. Por favor, esperá un momento.");
    } else {
      this.updateGame(this.gameId, this.game);
      this.updateGameState();
    }
  }


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.gameId = params.get('id')!;
      this.getGameById(this.gameId);
    });
    this.gameService.game$.subscribe(() => {
      this.getGameById(this.gameId);
    })
    console.log(this.players.length)
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
