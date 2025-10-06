import type {Country as ICountry} from "../interfaces/country.interface"
import {Continent} from "../../enums/continent"

export class CountryModel implements ICountry {
  id: string
  name: string
  pathIds: string[]
  color: string
  continent: string
  troops: number
  playerId?: string
  playerName?: string
  isSelected?: boolean
  isHovered?: boolean

  constructor(data: Partial<ICountry> & { id: string; name: string; continent: string }) {
    this.id = data.id
    this.name = data.name
    this.continent = data.continent
    this.pathIds = data.pathIds || []
    this.color = data.color || this.getDefaultColorForContinent(data.continent)
    this.troops = data.troops || 1
    this.playerId = data.playerId
    this.playerName = data.playerName
    this.isSelected = data.isSelected || false
    this.isHovered = data.isHovered || false
  }

  private getDefaultColorForContinent(continent: string): string {
    const continentColors: Record<string, string> = {
      [Continent.AMERICA_NORTE]: "#FFA500",
      [Continent.AMERICA_SUR]: "#90EE90",
      [Continent.EUROPA]: "#FFB6C1",
      [Continent.ASIA]: "#228B22",
      [Continent.AFRICA]: "#DC143C",
      [Continent.OCEANIA]: "#4169E1",
    }
    return continentColors[continent] || "#CCCCCC"
  }

  // Métodos útiles
  isOwnedBy(playerId: string): boolean {
    return this.playerId === playerId
  }

  canAttack(): boolean {
    return this.troops > 1
  }

  canReceiveTroops(): boolean {
    return true // Siempre puede recibir tropas
  }

  addTroops(amount: number): void {
    this.troops += amount
  }

  removeTroops(amount: number): boolean {
    if (this.troops - amount < 1) {
      return false // No se puede dejar sin tropas
    }
    this.troops -= amount
    return true
  }

  setOwner(playerId: string, playerName: string, color: string): void {
    this.playerId = playerId
    this.playerName = playerName
    this.color = color
  }

  clearOwner(): void {
    this.playerId = undefined
    this.playerName = undefined
    this.color = this.getDefaultColorForContinent(this.continent)
  }

  select(): void {
    this.isSelected = true
  }

  deselect(): void {
    this.isSelected = false
  }

  hover(): void {
    this.isHovered = true
  }

  unhover(): void {
    this.isHovered = false
  }

  // Convertir a objeto plano para serialización
  toJSON(): ICountry {
    return {
      id: this.id,
      name: this.name,
      pathIds: [...this.pathIds],
      color: this.color,
      continent: this.continent,
      troops: this.troops,
      playerId: this.playerId,
      playerName: this.playerName,
      isSelected: this.isSelected,
      isHovered: this.isHovered,
    }
  }

  // Crear desde objeto plano
  static fromJSON(data: ICountry): CountryModel {
    return new CountryModel(data)
  }

  // Clonar país
  clone(): CountryModel {
    return new CountryModel(this.toJSON())
  }
}
