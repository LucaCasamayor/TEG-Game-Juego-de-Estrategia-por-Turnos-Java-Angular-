import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  type ElementRef,
  EventEmitter,
  Input,
  type OnDestroy,
  type OnInit,
  Output,
  ViewChild,
} from "@angular/core"
import {CommonModule} from "@angular/common"
import {Subscription} from "rxjs"
import {MapService} from "../../services/map/map.service"
import type {Country, MapInteractionEvent} from "../../core/models/interfaces/country.interface"
import type {ContinentInfo} from "../../core/models/interfaces/map-config.interface"
import {CONTINENT_COLORS} from "../../core/config/teg-countries.config"

@Component({
  selector: "app-map",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./map.component.html",
  styleUrls: ["./map.component.css"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MapComponent implements OnInit, OnDestroy {
  @Input() svgPath = "./assets/images/game/Map.svg"
  @Input() showCountryInfo = true
  @Input() enableSelection = true
  @Input() enableHover = true

  @Output() countryInteraction = new EventEmitter<MapInteractionEvent>()
  @Output() countrySelected = new EventEmitter<Country>()
  @Output() countryHovered = new EventEmitter<Country | null>()

  @ViewChild("svgContainer", { static: true }) svgContainer!: ElementRef<HTMLDivElement>

  selectedCountry: Country | null = null
  hoveredCountry: Country | null = null
  countries: Country[] = []
  isLoading = false
  svgContent = ""
  showCountryNames = false

  // Mapa para almacenar los colores originales de los path border
  private originalBorderFillColors = new Map<string, string>()
  // Control del país actualmente en hover
  private currentHoveredCountryId: string | null = null

  private sub = new Subscription();

  constructor(
    private mapService: MapService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.subscribeToMapState()
    this.loadMap()
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  private subscribeToMapState(): void {
    this.mapService.mapState$.subscribe((state) => {
      this.selectedCountry = state.selectedCountry
      this.hoveredCountry = state.hoveredCountry
      this.countries = state.countries
      this.isLoading = state.isLoading
      this.cdr.markForCheck()
    })
  }

  private loadMap(): void {
    this.mapService
      .loadSvgMap(this.svgPath)
      .subscribe((svgContent) => {
        const container = this.svgContainer.nativeElement;
        container.innerHTML = svgContent;

        this.setupSvgInteractions()
        this.cdr.markForCheck()
      })
  }

  /**
   * Valida si un ID corresponde a un país válido (número del 1 al 50)
   */
  private isValidCountryId(id: string): boolean {
    const numericId = parseInt(id, 10);
    return !isNaN(numericId) && numericId >= 1 && numericId <= 50;
  }

  private setupSvgInteractions(): void {
    setTimeout(() => {
      const container = this.svgContainer.nativeElement;
      const svgElement = container.querySelector("svg");
      if (!svgElement) return;

      svgElement.style.height = "95vh"
      svgElement.style.width = "100vw"
      svgElement.style.objectFit = "contain"

      // Buscar todos los grupos <g> que representan países
      const countryGroups = svgElement.querySelectorAll("g[id]");

      countryGroups.forEach((group) => {
        const groupElement = group as SVGGElement;
        const countryId = groupElement.id;

        // Solo procesar si el ID es un número válido del 1 al 50
        if (!this.isValidCountryId(countryId)) {
          return; // Saltar este elemento
        }

        // Configurar estilos básicos del grupo
        groupElement.style.pointerEvents = "all";
        groupElement.style.cursor = "pointer";

        // Almacenar color original del path border
        this.storeBorderOriginalColor(groupElement, countryId);

        // Configurar eventos de hover únicamente
        groupElement.addEventListener("mouseenter", (event) => {
          if (this.enableHover) {
            this.handleCountryHover(countryId, event);
          }
        });

        groupElement.addEventListener("mouseleave", (event) => {
          if (this.enableHover) {
            this.handleCountryLeave(countryId, event);
          }
        });

        // Evento adicional para asegurar limpieza en el contenedor SVG
        groupElement.addEventListener("mousemove", (event) => {
          if (this.enableHover) {
            this.handleCountryHover(countryId, event);
          }
        });
      });

      // Evento global para limpiar hover cuando el mouse sale del SVG
      if (svgElement) {
        svgElement.addEventListener("mouseleave", () => {
          this.clearCurrentHover();
        });
      }
    }, 500);
  }

  private storeBorderOriginalColor(groupElement: SVGGElement, countryId: string): void {
    // Buscar el path border dentro del grupo
    const borderPath = groupElement.querySelector('path[id*="border"]') as SVGPathElement;
    if (borderPath) {
      const originalFill = borderPath.style.fill ||
        borderPath.getAttribute('fill') ||
        '#000000';
      this.originalBorderFillColors.set(countryId, originalFill);
    }
  }

  private handleCountryHover(countryId: string, event: MouseEvent): void {
    // Validar que sea un ID de país válido antes de procesar
    if (!this.isValidCountryId(countryId)) {
      return;
    }

    // Si ya hay un país en hover y es diferente al actual, limpiarlo primero
    if (this.currentHoveredCountryId && this.currentHoveredCountryId !== countryId) {
      this.clearCurrentHover();
    }

    // Si el país actual ya está en hover, no hacer nada
    if (this.currentHoveredCountryId === countryId) {
      return;
    }

    const svgElement = this.svgContainer.nativeElement.querySelector("svg");
    if (!svgElement) return;

    const groupElement = svgElement.querySelector(`g[id="${countryId}"]`) as SVGGElement;
    if (!groupElement) return;

    this.currentHoveredCountryId = countryId;

    // Aplicar efecto hover al path border
    this.applyBorderBrightnessEffect(groupElement, true);

    const country = this.getCountryInfo(countryId);
    if (country) {
      this.onCountryHover(country, event);
    } else {
      console.log(countryId);
    }
  }

  private handleCountryLeave(countryId: string, event: MouseEvent): void {
    // Validar que sea un ID de país válido antes de procesar
    if (!this.isValidCountryId(countryId)) {
      return;
    }

    // Solo procesar si el país que se está dejando es el actual en hover
    if (this.currentHoveredCountryId !== countryId) {
      return;
    }

    // Limpiar el hover
    this.clearCurrentHover();

    const country = this.getCountryInfo(countryId);
    if (country) {
      this.onCountryLeave(country, event);
    }
  }

  private clearCurrentHover(): void {
    if (!this.currentHoveredCountryId) return;

    // Validar que el ID actual sea válido antes de procesar
    if (!this.isValidCountryId(this.currentHoveredCountryId)) {
      this.currentHoveredCountryId = null;
      return;
    }

    const svgElement = this.svgContainer.nativeElement.querySelector("svg");
    if (!svgElement) return;

    const groupElement = svgElement.querySelector(`g[id="${this.currentHoveredCountryId}"]`) as SVGGElement;
    if (groupElement) {
      // Remover efecto hover del path border
      this.applyBorderBrightnessEffect(groupElement, false);
    }

    // Limpiar el estado
    this.currentHoveredCountryId = null;
  }

  private applyBorderBrightnessEffect(groupElement: SVGGElement, isHovering: boolean): void {
    const borderPath = groupElement.querySelector('path[id*="border"]') as SVGPathElement;
    if (!borderPath) return;

    const countryId = groupElement.id;

    // Validar que sea un ID de país válido antes de aplicar efectos
    if (!this.isValidCountryId(countryId)) {
      return;
    }

    if (isHovering) {
      // Aplicar efecto de brillo al fill del path border
      const originalColor = this.originalBorderFillColors.get(countryId) || '#000000';
      const brighterColor = this.lightenColor(originalColor, 0.4); // 40% más brillante
      borderPath.style.fill = brighterColor;
    } else {
      // Restaurar color original del path border
      const originalColor = this.originalBorderFillColors.get(countryId) || '#000000';
      borderPath.style.fill = originalColor;
    }
  }

  private lightenColor(color: string, amount: number): string {
    // Convertir color a RGB si es necesario
    let r: number, g: number, b: number;

    if (color.startsWith('#')) {
      // Formato hexadecimal
      const hex = color.slice(1);
      r = parseInt(hex.substr(0, 2), 16);
      g = parseInt(hex.substr(2, 2), 16);
      b = parseInt(hex.substr(4, 2), 16);
    } else if (color.startsWith('rgb')) {
      // Formato RGB
      const matches = color.match(/\d+/g);
      if (matches && matches.length >= 3) {
        r = parseInt(matches[0]);
        g = parseInt(matches[1]);
        b = parseInt(matches[2]);
      } else {
        return color; // Retornar color original si no se puede parsear
      }
    } else {
      // Color nombrado o formato no reconocido
      return color;
    }

    // Aplicar brillo (mezclar con blanco)
    r = Math.min(255, Math.round(r + (255 - r) * amount));
    g = Math.min(255, Math.round(g + (255 - g) * amount));
    b = Math.min(255, Math.round(b + (255 - b) * amount));

    // Convertir de vuelta a hexadecimal
    return `#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}`;
  }

  private onCountryHover(country: Country, event: MouseEvent): void {
    this.mapService.hoverCountry(country)

    const interactionEvent: MapInteractionEvent = {
      country,
      event,
      action: "hover",
    }

    this.countryInteraction.emit(interactionEvent)
    this.countryHovered.emit(country)
  }

  private onCountryLeave(country: Country, event: MouseEvent): void {
    this.mapService.hoverCountry(null)

    const interactionEvent: MapInteractionEvent = {
      country,
      event,
      action: "leave",
    }

    this.countryInteraction.emit(interactionEvent)
    this.countryHovered.emit(null)
  }

  getContinentName(continent: string | undefined): string {
    const names: Record<string, string> = {
      america_norte: "América del Norte",
      america_sur: "América del Sur",
      europa: "Europa",
      asia: "Asia",
      africa: "África",
      oceania: "Oceanía",
    }
    return names[continent || ""] || continent || "Desconocido"
  }

  getContinents(): ContinentInfo[] {
    return this.mapService.getContinentInfo()
  }

  getContinentColor(continentId: string): string {
    return CONTINENT_COLORS[continentId as keyof typeof CONTINENT_COLORS] || "#CCCCCC"
  }

  toggleCountryNames(): void {
    this.showCountryNames = !this.showCountryNames
    console.log("🏷️ Mostrar nombres:", this.showCountryNames)
  }

  updateCountry(countryId: string, updates: Partial<Country>): void {
    if (!this.isValidCountryId(countryId)) {
      console.warn(`ID de país inválido: ${countryId}. Solo se permiten números del 1 al 50.`);
      return;
    }

    if (updates.troops !== undefined) {
      this.mapService.updateCountryTroops(countryId, updates.troops)
    }

    if (updates.playerId && updates.playerName && updates.color) {
      this.mapService.updateCountryOwner(countryId, updates.playerId, updates.playerName, updates.color)
    }
  }

  getCountryInfo(countryId: string): Country | undefined {
    if (!this.isValidCountryId(countryId)) {
      return undefined;
    }

    return this.mapService.getCountryById(countryId)
  }
}
