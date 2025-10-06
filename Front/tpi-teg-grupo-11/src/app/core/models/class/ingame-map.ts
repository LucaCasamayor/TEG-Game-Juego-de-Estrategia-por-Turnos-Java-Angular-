import type {ContinentInfo, MapState} from "../interfaces/map-config.interface"
import {CountryModel} from "./country"
import {CONTINENT_BONUSES, CONTINENT_COLORS, TEG_COUNTRIES_CONFIG} from "../../config/teg-countries.config"

export class GameMapModel {
  private countries: Map<string, CountryModel> = new Map()
  private selectedCountry: CountryModel | null = null
  private hoveredCountry: CountryModel | null = null
  private isLoading = false
  private error?: string

  constructor() {
    this.initializeCountries()
  }

  private initializeCountries(): void {
    TEG_COUNTRIES_CONFIG.forEach((config) => {
      const country = new CountryModel({
        id: config.id,
        name: config.name,
        continent: config.continent,
        color: config.defaultColor,
        troops: Math.floor(Math.random() * 3) + 1, // 1-3 tropas iniciales
      })
      this.countries.set(config.id, country)
    })
  }

  // Getters
  getAllCountries(): CountryModel[] {
    return Array.from(this.countries.values())
  }

  getCountryById(id: string): CountryModel | undefined {
    return this.countries.get(id)
  }

  getCountriesByContinent(continent: string): CountryModel[] {
    return this.getAllCountries().filter((country) => country.continent === continent)
  }

  getSelectedCountry(): CountryModel | null {
    return this.selectedCountry
  }

  getHoveredCountry(): CountryModel | null {
    return this.hoveredCountry
  }

  getCountriesByPlayer(playerId: string): CountryModel[] {
    return this.getAllCountries().filter((country) => country.isOwnedBy(playerId))
  }

  // Setters
  selectCountry(countryId: string): boolean {
    const country = this.countries.get(countryId)
    if (!country) return false

    // Deseleccionar país anterior
    if (this.selectedCountry) {
      this.selectedCountry.deselect()
    }

    // Seleccionar nuevo país
    this.selectedCountry = country
    country.select()
    return true
  }

  clearSelection(): void {
    if (this.selectedCountry) {
      this.selectedCountry.deselect()
      this.selectedCountry = null
    }
  }

  hoverCountry(countryId: string | null): void {
    // Quitar hover del país anterior
    if (this.hoveredCountry) {
      this.hoveredCountry.unhover()
    }

    if (countryId) {
      const country = this.countries.get(countryId)
      if (country) {
        this.hoveredCountry = country
        country.hover()
      }
    } else {
      this.hoveredCountry = null
    }
  }

  // Operaciones de juego
  updateCountryTroops(countryId: string, troops: number): boolean {
    const country = this.countries.get(countryId)
    if (!country) return false

    country.troops = Math.max(1, troops) // Mínimo 1 tropa
    return true
  }

  updateCountryOwner(countryId: string, playerId: string, playerName: string, color: string): boolean {
    const country = this.countries.get(countryId)
    if (!country) return false

    country.setOwner(playerId, playerName, color)
    return true
  }

  moveTroops(fromCountryId: string, toCountryId: string, troopCount: number): boolean {
    const fromCountry = this.countries.get(fromCountryId)
    const toCountry = this.countries.get(toCountryId)

    if (!fromCountry || !toCountry) return false
    if (!fromCountry.removeTroops(troopCount)) return false

    toCountry.addTroops(troopCount)
    return true
  }

  // Información de continentes
  getContinentInfo(): ContinentInfo[] {
    const continents: Record<string, ContinentInfo> = {}

    this.getAllCountries().forEach((country) => {
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

  // Estado del mapa
  getMapState(): MapState {
    return {
      selectedCountry: this.selectedCountry?.toJSON() || null,
      hoveredCountry: this.hoveredCountry?.toJSON() || null,
      countries: this.getAllCountries().map((country) => country.toJSON()),
      isLoading: this.isLoading,
      error: this.error,
    }
  }

  setLoading(loading: boolean): void {
    this.isLoading = loading
  }

  setError(error: string | undefined): void {
    this.error = error
  }

  // Validaciones
  canAttackFrom(fromCountryId: string, toCountryId: string): boolean {
    const fromCountry = this.countries.get(fromCountryId)
    const toCountry = this.countries.get(toCountryId)

    if (!fromCountry || !toCountry) return false
    if (!fromCountry.canAttack()) return false
    if (fromCountry.playerId === toCountry.playerId) return false

    // Aquí podrías agregar lógica para verificar si los países son fronterizos
    return true
  }

  // Resetear mapa
  reset(): void {
    this.clearSelection()
    this.hoverCountry(null)
    this.countries.clear()
    this.initializeCountries()
    this.isLoading = false
    this.error = undefined
  }

  // Serialización
  toJSON() {
    return {
      countries: this.getAllCountries().map((country) => country.toJSON()),
      selectedCountry: this.selectedCountry?.toJSON() || null,
      hoveredCountry: this.hoveredCountry?.toJSON() || null,
      isLoading: this.isLoading,
      error: this.error,
    }
  }
}
