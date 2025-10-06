import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import {Settings} from '../../core/models/class/settings';
import {AIProfile} from '../../core/enums/ai-profile';
import {ObjectiveType} from '../../core/models/class/objective-type';
import {GameMap} from '../../core/models/class/game-map';
import {SettingsService} from '../../services/pre-game/settings.service';
import {Subscription} from 'rxjs';
import {NgForOf} from '@angular/common';
import {Game} from '../../core/models/class/game';
import {GameState} from '../../core/enums/game-state';
import {GameService} from '../../services/game/game.service';
import {SessionDirective} from '../../shared/directives/session/session.directive';
import {User} from '../../core/models/class/user';
import {Player} from '../../core/models/class/player';
import {Color} from '../../core/enums/color';
import {PlayerType} from '../../core/enums/player-type';
import {PlayerService} from '../../services/players/player.service';

@Component({
  selector: 'app-match-settings',
  imports: [FormsModule, NgForOf, SessionDirective],
  templateUrl: './match-settings.component.html',
  styleUrls: ['./match-settings.component.css']
})
export class MatchSettingsComponent implements OnInit, OnDestroy {

  constructor(
    private gameService: GameService,
    private settingService: SettingsService,
    private playerService: PlayerService,
    private router: Router
  ) {}

  private subscription = new Subscription();

  session: User | undefined;
  objectiveTypes: ObjectiveType[] = [];
  maps: GameMap[] = [];
  aiProfiles = Object.keys(AIProfile)
    .filter(key => isNaN(Number(key)))
    .map(key => ({ nombre: key, valor: AIProfile[key as keyof typeof AIProfile] }));
  selectedObjective: ObjectiveType | null = null;

  settings: Settings = {
    map: null as unknown as GameMap,
    objectiveTypes: [],
    turnTime: 60,
    aiProfile: this.aiProfiles[0].valor,
    isPrivate: false,
    password: ""
  };

  game: Game = {
    settings: this.settings,
    gameState: GameState.WAITING_PLAYERS,
    creationDate: new Date(),
    players: []
  };

  ngOnInit() {
    this.getAllObjectiveTypes();
    this.getGamesMaps();
  }

  handleUser(user: User) {
    this.session = user;
    console.log('Sesión recibida:', user);
  }

  getAllObjectiveTypes() {
    const handleObjectiveTypes = (objectiveTypes: ObjectiveType[]) => {
      this.objectiveTypes = objectiveTypes;
      const general = this.objectiveTypes.find(obj => obj.name === 'General');
      if (general) {
        this.settings.objectiveTypes = [general];
      }
      this.selectedObjective = this.objectiveTypes.find(obj => obj.name !== 'General') || null;
    };

    const cached = localStorage.getItem('objectiveTypes');
    if (!cached) {
      this.subscription.add(
        this.settingService.getAllObjectivesTypes().subscribe({
          next: (objectiveTypes) => {
            localStorage.setItem('objectiveTypes', JSON.stringify(objectiveTypes));
            handleObjectiveTypes(objectiveTypes);
          },
          error: (err) => alert(err.message)
        })
      );
    } else {
      handleObjectiveTypes(JSON.parse(cached));
    }
  }

  getGamesMaps() {
    const cached = localStorage.getItem('maps');
    if (!cached) {
      this.subscription.add(
        this.settingService.getAllGamesMap().subscribe({
          next: (maps) => {
            localStorage.setItem('maps', JSON.stringify(maps));
            this.maps = maps;
            if (maps.length) this.settings.map = maps[0];
          },
          error: (err) => alert(err.message)
        })
      );
    } else {
      this.maps = JSON.parse(cached);
      if (this.maps.length) this.settings.map = this.maps[0];
    }
  }

  generateShortId(length = 5): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    return Array.from({ length }, () => chars.charAt(Math.floor(Math.random() * chars.length))).join('');
  }

  changePrivacyParty(isPrivate: boolean) {
    this.settings.isPrivate = isPrivate;
    this.settings.password = isPrivate ? this.generateShortId() : "";
  }

  createGame(gameParam: Game) {
    this.subscription.add(
      this.gameService.createGame(gameParam).subscribe({
        next: (game) => {
          alert("Juego creado");
          this.game = game;

          if (!this.game.gameId) {
            alert("Error: gameId no está definido");
            return;
          }

          const firstPlayer: Player = {
            user: this.session,
            gameId: this.game.gameId!,
            playerColor: Color.RED,
            turnOrder: 0,
            isWinner: false,
            hasLost: false,
            playerType: PlayerType.PLAYER
          };

          this.createPlayer(firstPlayer);
        },
        error: (err) => alert(err.error.message)
      })
    );
  }

  createPlayer(player: Player) {
    this.subscription.add(
      this.playerService.createPlayer(player).subscribe({
        next: (player) => {
          alert("Primer jugador creado");

          if (!this.game) {
            console.error("this.game no está definido");
            return;
          }

          this.game.players = [player];

          if (this.game.gameId) {
            this.router.navigate(['/lobby', this.game.gameId]);
          } else {
            console.error("gameId no está definido");
          }
        },
        error: (err) => alert(err.message)
      })
    );
  }


  createSettings(settingsForm: NgForm) {
    if (settingsForm.invalid) {
      alert("Formulario inválido");
      return;
    }

    const general = this.objectiveTypes.find(obj => obj.name === 'General');
    if (!general || !this.selectedObjective) {
      alert("Debes seleccionar un objetivo además de General");
      return;
    }

    this.settings.objectiveTypes = [general, this.selectedObjective];

    this.subscription.add(
      this.settingService.createSettings(this.settings).subscribe({
        next: (setting) => {
          alert("Configuración creada");
          this.settings = setting;
          this.game.settings = setting;
          this.createGame(this.game);
        },
        error: (err) => alert(err.error.message)
      })
    );
  }

  onSubmit(settingsForm: NgForm) {
    this.createSettings(settingsForm);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
