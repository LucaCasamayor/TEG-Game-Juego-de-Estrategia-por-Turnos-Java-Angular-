import { Component, Input, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subject, forkJoin, takeUntil, interval, firstValueFrom } from 'rxjs';
import { MovementDisplay } from '../../core/models/class/movement-display';
import { Territory } from '../../core/models/class/game-data';
import { GameService } from '../../services/game/game.service';
import { HistoryService } from '../../services/history/history.service';
import { TurnPhase } from '../../core/enums/turn-phase';
import { Turn } from '../../core/models/class/turn';
import { Player } from '../../core/models/class/player';

@Component({
  selector: 'app-movs-historial',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movs-historial.component.html',
  styleUrls: ['./movs-historial.component.css']
})
export class MovsHistorialComponent implements OnInit, OnDestroy, OnChanges {
  @Input() gameId!: string;
  @Input() mapId!: number;
  @Input() refreshTrigger?: any;
  @Input() currentTurnNumber?: number;

  movementDisplays: MovementDisplay[] = [];
  movementPlayerMap = new Map<number, string>(); // movementId → playerName
  movementTurnMap = new Map<number, number>();   // movementId → turnNumber

  territories: Territory[] = [];

  private lastMovementCount = 0;
  private destroy$ = new Subject<void>();

  constructor(
      private gameService: GameService,
      private historyService: HistoryService
  ) {}

  ngOnInit(): void {
    this.loadMovementHistory();
    this.startAutoRefresh();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['gameId'] || changes['refreshTrigger']) {
      this.loadMovementHistory();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private startAutoRefresh(): void {
    interval(3000).pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.checkForNewMovements();
    });
  }

  private checkForNewMovements(): void {
    this.historyService.getMovementsDisplayByGame(this.gameId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (movements) => {
            const validMovements = this.filterValidMovements(movements);
            if (validMovements.length !== this.lastMovementCount) {
              this.movementDisplays = validMovements.reverse();
              this.lastMovementCount = validMovements.length;
              this.updatePlayerMap(); // actualizar map si cambian movimientos
            }
          },
          error: (err) => {
            console.error('Error al chequear movimientos nuevos', err);
          }
        });
  }

  loadMovementHistory(): void {
    forkJoin({
      //Llama a todos los service necesarios al mismo tiempo
      movements: this.historyService.getMovementsDisplayByGame(this.gameId),
      territories: this.historyService.getAllByMapId(this.mapId || 1),
      turns: this.gameService.getTurnsByGame(this.gameId)

    }).pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (data) => {

            const validMovements = this.filterValidMovements(data.movements);
            this.movementDisplays = validMovements.reverse();
            this.territories = data.territories;
            this.lastMovementCount = validMovements.length;

            this.buildMovementPlayerMap().then(() => {
              this.movementDisplays = validMovements;
              this.territories = data.territories;
              this.lastMovementCount = validMovements.length;
            });
          },
          error: (err) => {
            console.error('Error cargando mov historial: ', err);
          }
        });
  }


  private async buildMovementPlayerMap(): Promise<void> {
    this.movementPlayerMap.clear();
    this.movementTurnMap.clear();

    const turns = await firstValueFrom(this.gameService.getTurnsByGame(this.gameId));

    //Mapea el nombre del bot tambien, (esta pinchila aumentaba al finalizar cada turno)
    const botNameByPlayerId = new Map<number, string>();
    let botIndex = 1;

    // Calcular la cantidad fija de jugadores únicos al inicio
    const uniquePlayers = turns
        .map(t => t.player)
        .filter((player, index, arr) => arr.findIndex(p => p.playerId === player.playerId) === index);
    const totalPlayers = uniquePlayers.length;

    turns.forEach((turn, index) => {
      const player = turn.player;
      const playerId = player.playerId;
      const playerType = player.playerType.toString();

      let name: string;
      //Si el tipo de jugador no es player, setea el nombre con bot (numero de bot)
      if (playerType.toString().toLowerCase() === 'player') {
        name = player.user?.username!;
      } else {
        if (!botNameByPlayerId.has(playerId!)) {
          botNameByPlayerId.set(playerId!, `BOT ${botIndex++}`);
        }
        name = botNameByPlayerId.get(playerId!)!;
      }

      const turnNumber = Math.floor(index / totalPlayers) + 1; // ← usar número de turno global

      //Mapea los nombres y index del turno en cada movimiento para no ser actualizado con cada get desde el back
      turn.movements.forEach(movement => {
        if (movement.id != null) {
          this.movementPlayerMap.set(movement.id, name);
          this.movementTurnMap.set(movement.id, turnNumber);
        }
      });
    });
  }



  //Va actualizando el mapeo del player
  private updatePlayerMap(): void {
    this.gameService.getTurnsByGame(this.gameId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => this.buildMovementPlayerMap(),
          error: (err) => console.error('Error updating player map:', err)
        });
  }

  //Setea el nombre del objeto territorio
  getTerritoryNameById(id?: number): string {
    if (!id) return 'territorio desconocido';
    const terr = this.territories.find(t => Number(t.territoryId) === id);
    return terr?.name ?? 'territorio desconocido';
  }

  //Formatea el mensaje del historial
  formatMovement(movement: MovementDisplay): string {
    const from = this.getTerritoryNameById(movement.startTerritoryId);
    const to = this.getTerritoryNameById(movement.endTerritoryId);
    const count = movement.armyCount || 0;
    const name = this.movementPlayerMap.get(movement.id!) || 'Jugador';
    const turnNumber = this.movementTurnMap.get(movement.id!) || '?';

    switch (movement.movementType) {
      case TurnPhase.DEPLOYMENT:
        return `(TURNO: ${turnNumber}) ${name} DESPLEGÓ ${count} tropas en "${to}"`;

      case TurnPhase.ATTACK:
        const baseLine = `(TURNO: ${turnNumber}) ${name} ATACÓ a "${to}" desde "${from}" con ${count} tropas`;
        if (movement.dice?.length) {
          const diceLines = movement.dice
              .filter(d => d.attackerDice !== 0 && d.defenderDice !== 0) // 👈 filtro importante
              .map(d => `(⚔️${d.attackerDice} vs 🛡️${d.defenderDice})`)
              .join('\n');
          return diceLines ? `${baseLine}\n${diceLines}` : baseLine;
        }
        return baseLine;

      case TurnPhase.FORTIFY:
        return `(TURNO: ${turnNumber}) ${name} FORTIFICÓ "${to}" desde "${from}" con ${count} tropas`;

      case TurnPhase.DONE:
        return `(TURNO: ${turnNumber}) ${name} TERMINÓ SU TURNO`;

      default:
        return `(TURNO: ${turnNumber}) ${name} REALIZÓ UN MOVIMIENTO`;
    }
  }


  trackByMovementId(index: number, movement: MovementDisplay): number {
    return movement.id || index;
  }

  public forceRefresh(): void {
    this.loadMovementHistory();
  }

  //VALIDA MOVIMIENTOS
  private filterValidMovements(movements: MovementDisplay[]): MovementDisplay[] {
    return movements.filter(movement =>
        movement.id &&
        movement.movementType &&
        movement.armyCount !== undefined &&
        movement.armyCount !== null && movement.armyCount > 0
    );
  }
}
