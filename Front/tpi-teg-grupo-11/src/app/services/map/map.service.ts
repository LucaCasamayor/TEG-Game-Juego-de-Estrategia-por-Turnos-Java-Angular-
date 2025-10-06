import {Injectable} from "@angular/core"
import {BehaviorSubject, catchError, map, type Observable, of} from "rxjs"
import {Country} from "../../core/models/interfaces/country.interface"
import type {ContinentInfo, MapState} from "../../core/models/interfaces/map-config.interface"
import {CONTINENT_BONUSES, CONTINENT_COLORS, TEG_COUNTRIES_CONFIG} from "../../core/config/teg-countries.config"
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: "root",
})
export class MapService {
  private mapStateSubject = new BehaviorSubject<MapState>({
    selectedCountry: null,
    hoveredCountry: null,
    countries: [],
    isLoading: false,
  })

  public mapState$ = this.mapStateSubject.asObservable()
  public selectedCountry$ = this.mapState$.pipe(map((state) => state.selectedCountry))
  public hoveredCountry$ = this.mapState$.pipe(map((state) => state.hoveredCountry))
  public countries$ = this.mapState$.pipe(map((state) => state.countries))

  constructor(private http: HttpClient) {
    this.initializeCountries()
  }

  private initializeCountries(): void {
    const countries: Country[] = TEG_COUNTRIES_CONFIG.map((config) => ({
      id: config.id,
      name: config.name,
      pathIds: [],
      color: config.defaultColor,
      troops: Math.floor(Math.random() * 5) + 1,
      continent: config.continent,
      playerId: undefined,
      playerName: undefined,
    }))

    this.updateMapState({ countries })
  }

  loadSvgMap(svgPath: string): Observable<string> {
    this.updateMapState({ isLoading: true })

    return this.http.get(svgPath, { responseType: "text" }).pipe(
      map((svgContent) => {
        this.parseSvgAndUpdateCountries(svgContent)
        this.updateMapState({ isLoading: false })
        return svgContent
      }),
      catchError((error) => {
        console.error("Error loading SVG:", error)
        this.updateMapState({ isLoading: false, error: "Error cargando el mapa" })
        return of("")
      }),
    )
  }

  private parseSvgAndUpdateCountries(svgContent: string): void {
    const parser = new DOMParser()
    const svgDoc = parser.parseFromString(svgContent, "image/svg+xml")
    const currentState = this.mapStateSubject.value

    const updatedCountries = currentState.countries.map((country) => {
      const config = TEG_COUNTRIES_CONFIG.find((c) => c.id === country.id)
      if (!config) return country

      const pathIds: string[] = []

      config.pathSelectors.forEach((selector) => {
        const paths = svgDoc.querySelectorAll(selector)
        paths.forEach((path, index) => {
          const pathId = path.getAttribute("id") || `${country.id}-path-${index}`
          path.setAttribute("id", pathId)
          path.setAttribute("data-country-id", country.id)
          pathIds.push(pathId)
        })
      })

      return {
        ...country,
        pathIds,
      }
    })

    this.updateMapState({ countries: updatedCountries })
  }

  selectCountry(country: Country): void {
    this.updateMapState({ selectedCountry: country })
  }

  hoverCountry(country: Country | null): void {
    this.updateMapState({ hoveredCountry: country })
  }

  updateCountryTroops(countryId: string, troops: number): void {
    const currentState = this.mapStateSubject.value
    const updatedCountries = currentState.countries.map((country) =>
      country.id === countryId ? { ...country, troops } : country,
    )
    this.updateMapState({ countries: updatedCountries })
  }

  updateCountryOwner(countryId: string, playerId: string, playerName: string, color: string): void {
    const currentState = this.mapStateSubject.value
    const updatedCountries = currentState.countries.map((country) =>
      country.id === countryId ? { ...country, playerId, playerName, color } : country,
    )
    this.updateMapState({ countries: updatedCountries })
  }

  getCountryById(countryId: number | string): Country | undefined {
    return this.mapStateSubject.value.countries.find(
        (country) => country.id.toString() === countryId.toString()
    );
  }




  getCountriesByContinent(continent: string): Country[] {
    return this.mapStateSubject.value.countries.filter((country) => country.continent === continent)
  }

  private updateMapState(partialState: Partial<MapState>): void {
    const currentState = this.mapStateSubject.value
    this.mapStateSubject.next({ ...currentState, ...partialState })
  }

  getContinentColor(continent: string): string {
    return CONTINENT_COLORS[continent as keyof typeof CONTINENT_COLORS] || "#CCCCCC"
  }

  resetMapState(): void {
    this.initializeCountries()
    this.updateMapState({
      selectedCountry: null,
      hoveredCountry: null,
    })
  }

  getContinentInfo(): ContinentInfo[] {
    const continents: Record<string, ContinentInfo> = {}

    this.mapStateSubject.value.countries.forEach((country) => {
      if (!continents[country.continent]) {
        continents[country.continent] = {
          id: country.continent,
          name: this.getContinentDisplayName(country.continent),
          countries: 0,
          bonus: CONTINENT_BONUSES[country.continent as keyof typeof CONTINENT_BONUSES] || 0,
          color: CONTINENT_COLORS[country.continent as keyof typeof CONTINENT_COLORS] || "#CCCCCC",
        }
      }
      continents[country.continent].countries++
    })

    return Object.values(continents)
  }

  private getContinentDisplayName(continent: string): string {
    const names: Record<string, string> = {
      america_norte: "América del Norte",
      america_sur: "América del Sur",
      europa: "Europa",
      asia: "Asia",
      africa: "África",
      oceania: "Oceanía",
    }
    return names[continent] || continent
  }
}
