import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChildren,
  QueryList,
  ElementRef,
} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Subscription} from 'rxjs';
import {Player} from '../../core/models/class/player';
import {ActivatedRoute} from '@angular/router';
import {GameService} from '../../services/game/game.service';
import {Territory, TerritoryState} from '../../core/models/class/game-data';
import {TurnPhase} from '../../core/enums/turn-phase';
import {Turn} from '../../core/models/class/turn';
import {NgStyle} from '@angular/common';

@Component({
  selector: 'app-test-map',

  templateUrl: './test-map.component.html',
  imports: [
    NgStyle
  ],
  styleUrl: './test-map.component.css'
})
export class TestMapComponent implements OnChanges, OnInit {
  gameId: string = "";
  players : Player[] = [];

  @Input() svgPath!: any;
  @Input() turn : Turn | undefined;

  countries: any[] = [];
  svgSize: { width: number; height: number } = { width: 0, height: 0 };

  sub = new Subscription();
  tooltipX = 0;
  tooltipY = 0;
  tooltipCountryName = '';
  tooltipRegion = '';
  tooltipBackgroundColor = '#CCCCCC';
  showTooltipFlag = false;
  // Mapa de colores para los jugadores
  private colorMap = {
  RED : "#ff3c3c",
  BLUE : "#2563EB",
  GREEN : "#3dff58",
  YELLOW : "#fff413",
  PURPLE : "#9c61ff",
  ORANGE : "#ff6e15",
  };
  @Input() gameData: any; //recibir el objeto game
  @Output() clickedTerritory = new EventEmitter<TerritoryState>();

  @ViewChildren('countryPath') countryPathElements!: QueryList<ElementRef<SVGPathElement>>;

  constructor(
    private http: HttpClient,
    private gameService : GameService,
    private route : ActivatedRoute,
  ) { }

  ngOnInit() {
    this.sub.add(
      this.route.paramMap.subscribe(params => {
        const id = params.get('id');
        if (id) {
          this.gameId = id;
          this.loadTerritoriesStates();

        }
      })
    );

    this.sub.add(
      this.gameService.game$.subscribe(gameData => {
        this.players = gameData.players;
        this.updateTerritoryColors();
      })
    );
  }


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['svgPath'] && this.svgPath) {
      this.loadSvg(this.svgPath);
    }

    // ✅ Cuando cambian los datos del juego, actualizar el mapa
    if (changes['gameData'] && this.gameData) {
      this.players = this.gameData.players;
      this.updateTerritoryColors(); // Esto actualiza el armyCount en los countries
    }
  }

  loadTerritoriesStates() {
    this.sub = this.gameService.getGameDataById(this.gameId).subscribe({
      next: gameData => {
        this.players = gameData.players;
        this.updateTerritoryColors();
        this.setCountryCenters();
      },
      error: err => {
        alert(err.error.message);
      }
    })
  }

  loadSvg(svgPath: string) {
    this.http.get(svgPath, { responseType: 'text' }).subscribe((svgText) => {
      const parser = new DOMParser();
      const svgDoc = parser.parseFromString(svgText, 'image/svg+xml');

      const svgElement = svgDoc.querySelector('svg');
      if (svgElement) {
        this.svgSize = {
          width: parseFloat(svgElement.getAttribute('width') || '0'),
          height: parseFloat(svgElement.getAttribute('height') || '0')
        };
      }

      const paths = svgDoc.querySelectorAll('path[d]') as NodeListOf<SVGPathElement>;
      this.countries = [];

      // Centros manuales para territorios que no se centran bien automáticamente
      const manualCenters: Record<string, { cx: number; cy: number }> = {
        'California': { cx: 380, cy: 415 },
        'Nueva York': { cx: 450, cy: 360 },
        'Siberia': { cx: 1534, cy: 230 }
      };

      // Crear SVG temporal en DOM para cálculo bbox
      const tempSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
      document.body.appendChild(tempSvg);

      paths.forEach((pathEl) => {
        let cx: number;
        let cy: number;

        if (manualCenters[pathEl.id]) {
          // Usar posición manual si existe
          cx = manualCenters[pathEl.id].cx;
          cy = manualCenters[pathEl.id].cy;
        } else {
          // Calcular centro automáticamente
          const tempPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
          tempPath.setAttribute('d', pathEl.getAttribute('d')!);
          tempSvg.appendChild(tempPath);

          const bbox = tempPath.getBBox();
          cx = bbox.x + bbox.width / 2;
          cy = bbox.y + bbox.height / 2;

          tempSvg.removeChild(tempPath);
        }

        this.countries.push({
          id: this.countries.length + 1,
          name: pathEl.id,
          figure: pathEl.getAttribute('d'),
          fill: '#CCCCCC',
          stroke: 'BLACK',
          cx,
          cy,
          opacity: 1,
          regionName: 'Desconocida'
        });
      });

      document.body.removeChild(tempSvg);

      this.updateTerritoryColors();

    });
  }


  setCountryCenters() {
    this.countryPathElements.forEach((ref, index) => {
      const path = ref.nativeElement;
      const bbox = path.getBBox();
      this.countries[index].cx = bbox.x + bbox.width / 2;
      this.countries[index].cy = bbox.y + bbox.height / 2;
    });
  }




  updateTerritoryColors() {
    if (!this.players || !this.countries) return;

    const territoryOwnerMap = new Map<string, Player>();

    this.players.forEach(player => {
      if (player.territories) {
        player.territories.forEach(territoryState => {
          territoryOwnerMap.set(territoryState.territory.name, player);
        });
      }
    });

    this.countries.forEach(country => {
      const owner = territoryOwnerMap.get(country.name);
      if (owner) {
        const territoryState = owner.territories?.find(t => t.territory.name === country.name);

        country.fill = this.colorMap[owner.playerColor as keyof typeof this.colorMap] || '#CCCCCC';
        country.armyCount = territoryState?.armyCount || 0;
        country.owner = owner;
        country.regionName = territoryState?.territory.region.name || 'Desconocida';
      } else {
        country.regionName = 'Desconocida';
        country.fill = 'RED';
        country.armyCount = 0;
        country.owner = null;
      }
    });

  }

  // TODO traer los borders de la API
  makeBordersBigger(country:any) {
    const ownerTerritories : TerritoryState[] = country.owner!.territories!;
    const countryBorders = ownerTerritories.find(t => t.territoryStateId === country.id)?.territory.bordersId;
    if (countryBorders && countryBorders.length > 0) {
      this.countries.forEach(c => {
        country.stroke = 'orange';
        country.strokeWidth = 5;
        country.opacity = 1;
        if(this.gameData?.currentTurn.turnPhase === TurnPhase.ATTACK) {
          if ((countryBorders!.includes(c.id) || c.id === country.id) && c.owner && c.owner.playerId !== country.owner!.playerId) {
            c.stroke = 'orange';
            c.strokeWidth = 5;
            c.opacity = 1;
          } else {
            c.stroke = 'black';
            c.strokeWidth = 1;
          }
        }
        else if(this.gameData?.currentTurn.turnPhase === TurnPhase.FORTIFY) {
          if ((countryBorders!.includes(c.id) || c.id === country.id) && c.owner && c.owner.playerId === country.owner!.playerId) {
            c.stroke = 'orange';
            c.strokeWidth = 5;
            c.opacity = 1;
          } else {
            c.stroke = 'black';
            c.strokeWidth = 1;
          }
        }
      });
    }
  }

  parseCountryToTerritoryState(country: any) {

    const territoryFromTerritoryState : Territory = country.owner!.territories!.find((t:TerritoryState) => t.territoryStateId === country.id)?.territory;

    const countryConverted : TerritoryState = {
      territoryStateId: country.id,
      turn: this.turn as Turn,
      player: country.owner,
      armyCount: country.armyCount,
      territory: territoryFromTerritoryState,
    }

    return countryConverted;
  }

  countryClick(id: string) {
    const country = this.countries.find(c => c.id === id);
    if (country && country.owner) {
      if((this.turn?.turnPhase === TurnPhase.ATTACK || this.turn?.turnPhase === TurnPhase.FORTIFY) && country.owner.playerId === this.turn?.player.playerId) {
        this.makeBordersBigger(country);
      }
      this.clickedTerritory.emit(this.parseCountryToTerritoryState(country));
    }
  }
  showTooltip(event: MouseEvent, name: string, region: string) {
    this.tooltipX = event.clientX;
    this.tooltipY = event.clientY;
    this.tooltipCountryName = name;
    this.tooltipRegion = region;

    const country = this.countries.find(c => c.name === name);
    if (country && country.owner) {
      const playerColor = country.owner.playerColor as keyof typeof this.colorMap;
      this.tooltipBackgroundColor = this.colorMap[playerColor] || '#CCCCCC';
    } else {
      this.tooltipBackgroundColor = '#CCCCCC';
    }

    this.showTooltipFlag = true;
  }

  hideTooltip() {
    this.showTooltipFlag = false;
  }
}
