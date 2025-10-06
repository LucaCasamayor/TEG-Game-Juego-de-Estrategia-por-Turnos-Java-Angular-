import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subject, Subscription, takeUntil} from 'rxjs';
import {GameService} from '../../services/game/game.service';
import {GameData, TerritoryState} from '../../core/models/class/game-data';
import {GamePlayersBarComponent} from '../../shared/utils/game-players-bar/game-players-bar.component';
import {TestMapComponent} from '../test-map/test-map.component';
import {MatDialog} from '@angular/material/dialog';
import {DeployTroopsModalComponent} from '../../shared/deploy-troops-modal/deploy-troops-modal.component';
import {TurnPhase} from '../../core/enums/turn-phase';
import {Movement} from '../../core/models/class/movement';
import {AttackTroopsModalComponent} from '../../shared/attack-troops-modal/attack-troops-modal.component';
import {MovsHistorialComponent} from '../../shared/movs-historial/movs-historial.component';
import {PlayerType} from '../../core/enums/player-type';
import {FortifyTroopsModalComponent} from '../../shared/fortify-troops-modal/fortify-troops-modal.component';
import {GameState} from '../../core/enums/game-state';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  imports: [
    GamePlayersBarComponent,
    TestMapComponent,
    MovsHistorialComponent,
  ],
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  private sub = new Subscription();
  protected readonly TurnPhase = TurnPhase;
  private isBotTurn : boolean = false;
  private botIsPlaying : boolean = false;

  game: GameData | null = null;
  gameId: string = '';
  loading: boolean = true;
  error: string | null = null;

  svgPath: string = 'assets/images/game/mapaa.svg';
  svgLoaded: boolean = false;
  shouldLoadSvg: boolean = true; // solo se pasa el svgPath 1 vez


  nextMovement: Movement = {
    armyCount: 0,
    movementType: null // o el TurnPhase que corresponda
  }
  borders: number[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameService: GameService,
    private dialog: MatDialog,
  ) {
  }

  public loadGame(gameId: string): void {
    this.loading = true;
    this.error = null;

    this.gameService.getGameDataById(gameId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (game) => {
          const isFirstLoad = this.game == null;
          console.log(game)
          this.game = game;
          if(this.game.gameState === GameState.FINISHED) {
            alert("GAME OVER")
            this.router.navigate(['/']);
          }
          this.loading = false;

          if (isFirstLoad){
            this.svgLoaded = true;
          } else {
            this.shouldLoadSvg = true;
          }

          this.nextMovement = {
            armyCount: 0,
            movementType: null
          }

          this.isBotTurn = game.currentTurn.player.playerType === PlayerType.BOT;
          console.log(this.isBotTurn);
          this.botIsPlaying = false;
          if (this.isBotTurn) {
            this.triggerBotTurn();
          }
        },
        error: (error) => {
          console.error('Error loading game:', error);
          this.error = 'Error al cargar el juego. Por favor, intenta nuevamente.';
          this.loading = false;
        }
      });
  }


  getPlayerTurn() {
    return this.game!.players.find(p => p.turnOrder === this.game!.currentTurnIndex);
  }

  onBackToMenu(): void {
    this.router.navigate(['/']);
  }

  onGameAction(phase: TurnPhase) {
    console.log(this.nextMovement)

    if (this.game) {
      if (phase === TurnPhase.DONE) {
        console.log("End phase")
        this.gameService.updateGamePhase(this.gameId, this.game).subscribe({
          next: game => {
            this.game = game;
          },
          error: (err) => {
            alert(err.error.message)
          }
        });
      }
      else {
          this.game.currentTurn.movements.push(this.nextMovement);
          console.log(this.game.currentTurn.movements);
          this.gameService.updateGameInGame(this.gameId, this.game).subscribe({
            next: game => {
              this.game = game;
            },
            error: (err) => {
              alert(err.error.message)
            }
          });
      }
    }
  }

  openModal(phase: TurnPhase, territory: any, availableTroops: number) {
    if (phase === TurnPhase.DEPLOYMENT) {
      console.log("MODAL DEPLOY EJECUTADO")
      console.log(territory)
      const dialogRef = this.dialog.open(DeployTroopsModalComponent, {
        data: {maxTroops: availableTroops}
      });

      dialogRef.afterClosed().subscribe((selectedTroops: number | null) => {
        if (selectedTroops !== null) {
          if (selectedTroops > availableTroops) {
            alert(`No puedes usar más de ${availableTroops} tropas`);
          } else {
            console.log(`Tropas usadas: ${selectedTroops}`);
            this.nextMovement = {
              armyCount: selectedTroops,
              // startTerritoryId: territory.id,

              movementType: null, // o el que corresponda

              endTerritoryId: territory.territoryStateId,
            }
            this.onGameAction(TurnPhase.DEPLOYMENT)
          }
        } else {
          console.log('Despliegue cancelado');
        }
      });
    }
    else if (phase === TurnPhase.ATTACK) {
      console.log("MODAL ATTACK EJECUTADO", territory)
      const dialogRef = this.dialog.open(AttackTroopsModalComponent, {
        data: {maxTroops: availableTroops}
      });
      dialogRef.afterClosed().subscribe((selectedTroops: number | null) => {
        if (selectedTroops !== null) {
          if (selectedTroops > availableTroops || selectedTroops < 1) {
            alert(`No puedes atacar con más de ${availableTroops - 1} tropas y menos de 1 tropas`);
          } else {
            console.log(`Tropas usadas: ${selectedTroops}`);
            console.log(this.game)
            this.onGameAction(TurnPhase.ATTACK)
          }
        } else {
          console.log('Ataque cancelado');
        }
      });
    }
    else if (phase === TurnPhase.FORTIFY) {
      console.log("MODAL FORTIFY EJECUTADO", territory)
      const dialogRef = this.dialog.open(FortifyTroopsModalComponent, {
        data: {maxTroops: availableTroops}
      });
      dialogRef.afterClosed().subscribe((selectedTroops: number | null) => {
        if (selectedTroops !== null) {
          if (selectedTroops > availableTroops || selectedTroops < 1) {
            alert(`No puedes fortificar con más de ${availableTroops - 1} tropas y menos de 1 tropas`);
          } else {
            console.log(`Tropas usadas: ${selectedTroops}`);
            this.nextMovement.armyCount = selectedTroops;
            console.log(this.nextMovement)
            this.onGameAction(TurnPhase.FORTIFY)
          }
        } else {
          console.log('Fortify cancelado');
        }
      });
    }
  }

  deploy(isPlayerTerritory: boolean, territory: TerritoryState) {
    const availableTroops = this.game!.currentTurn.movements[0].armyCount;
    if (isPlayerTerritory) {
      this.openModal(TurnPhase.DEPLOYMENT, territory, availableTroops!);
    } else {
      alert('No puedes desplegar en territorio ajeno')
    }
  }

  attack() {
    const allTerritoriesStates = this.game!.players.flatMap(p => p.territories);
    if (allTerritoriesStates.length !== 0) {
      const territoryStart = allTerritoriesStates.find(t => t?.territoryStateId === this.nextMovement.startTerritoryId);
      if (territoryStart) {
        this.borders = territoryStart.territory.bordersId!;

        console.log(this.nextMovement.endTerritoryId, territoryStart.armyCount);

        if (this.nextMovement.endTerritoryId && territoryStart.armyCount > 1) {
          console.log("entró")
          this.openModal(TurnPhase.ATTACK, this.nextMovement.endTerritoryId, territoryStart.armyCount);
        }
      } else {
        alert('No se encontró el territoryState')
      }
    }
  }

  fortify() {
    const allTerritoriesStates = this.game!.players.flatMap(p => p.territories);
    if (allTerritoriesStates.length !== 0) {
      const territoryStart = allTerritoriesStates.find(t => t?.territoryStateId === this.nextMovement.startTerritoryId);
      if (territoryStart) {
        this.borders = territoryStart.territory.bordersId!;

        console.log(this.nextMovement.endTerritoryId, territoryStart.armyCount);

        if (this.nextMovement.endTerritoryId && territoryStart.armyCount > 1) {
          this.openModal(TurnPhase.FORTIFY, this.nextMovement.endTerritoryId, territoryStart.armyCount - 1);
        }
      } else {
        alert('No se encontró el territoryState')
      }
    }
  }

  getTerritoryNameById(id: number): string {
    const allTerritories = this.game?.territories || [];
    return allTerritories.find(t => t.territoryId! === id)?.name ?? 'Territorio desconocido';
  }

  onClickTerritory(territory: TerritoryState) {

    const isPlayerTurn = this.game!.currentTurn.player.turnOrder === this.game!.currentTurnIndex;
    const isPlayerTerritory = this.game!.currentTurn.player.playerId === territory.player.playerId;
    const currentTurn = this.getCurrentTurn();

    if (!isPlayerTurn) {
      alert("¡No es tu turno!");
      return;
    }

    if(currentTurn.turnPhase === TurnPhase.DEPLOYMENT) {
      this.deploy(isPlayerTerritory, territory);
    }
    else if(currentTurn.turnPhase === TurnPhase.ATTACK) {
      if(!this.nextMovement.startTerritoryId && territory.player.playerId === this.game!.currentTurn.player.playerId) {
        this.nextMovement = {
          movementType: null, // o el que corresponda

          startTerritoryId: territory.territoryStateId,
          armyCount: territory.armyCount
        };
      }
      else if(this.nextMovement.startTerritoryId
        && territory.player.playerId !== this.game!.currentTurn.player.playerId
        && territory.territory.bordersId!.includes(this.nextMovement.startTerritoryId)) {

        console.log("BORDER VALIDO")
        this.nextMovement = {
          ...this.nextMovement,
          endTerritoryId: territory.territoryStateId
        };
        this.attack();
      }
    }
    else if(currentTurn.turnPhase === TurnPhase.FORTIFY) {
      console.log("fortify")
      if(!this.nextMovement.startTerritoryId && territory.player.playerId === this.game!.currentTurn.player.playerId && territory.armyCount > 1) {
        this.nextMovement = {
          movementType: null, // o el que corresponda

          startTerritoryId: territory.territoryStateId,
          armyCount: territory.armyCount
        };
        console.log(this.nextMovement);
      }
      else if(this.nextMovement.startTerritoryId && territory.player.playerId === this.game!.currentTurn.player.playerId && territory.territory.bordersId!.includes(this.nextMovement.startTerritoryId)) {
        console.log("BORDER VALIDO")
        this.nextMovement = {
          ...this.nextMovement,
          endTerritoryId: territory.territoryStateId
        };
        this.fortify();
      }
    }
  }

  ngOnInit(): void {
    this.route.paramMap
        .pipe(takeUntil(this.destroy$))
        .subscribe(params => {
          const id = params.get('id');
          if (id) {
            this.gameId = id;
            this.loadGame(id); // Solo una vez al inicio
          } else {
            this.error = 'ID de juego no válido';
            this.loading = false;
          }
        });

    // ✅ Solo actualizamos el game local, sin llamar a loadGame
    this.sub.add(
        this.gameService.game$.subscribe((updatedGame) => {
          this.game = updatedGame;
        })
    );
    this.gameService.game$.subscribe((updatedGame) => {
      this.loadGame(this.gameId);
    })
  }


  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.sub.unsubscribe();
  }

  getCurrentTurn() {
    return this.game!.currentTurn;
  }

  triggerBotTurn() {
    console.log("Turno del bot ejecutado")
    if (!this.game || !this.gameId) return;
    this.botIsPlaying = true;
    setTimeout(() => {
      this.gameService.updateGameInGame(this.gameId, this.game!).subscribe({
        next: (updatedGame) => {
          this.game = updatedGame;

          this.isBotTurn = updatedGame.currentTurn.player.playerType === PlayerType.BOT;

          if (this.isBotTurn && !this.botIsPlaying) {
            this.triggerBotTurn();
          }
        },
        error: (err) => {
          console.error("Error ejecutando turno del bot:", err);
        }
      });
    }, 1500);
  }
}


