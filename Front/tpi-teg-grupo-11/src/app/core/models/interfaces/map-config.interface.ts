export interface MapConfig {
  svgPath: string
  viewBox: string
  width: number
  height: number
  countries: CountryConfig[]
}

export interface CountryConfig {
  id: string
  name: string
  pathSelectors: string[]
  continent: string
  defaultColor: string
}

export interface MapState {
  selectedCountry: Country | null
  hoveredCountry: Country | null
  countries: Country[]
  isLoading: boolean
  error?: string
}

export interface ContinentInfo {
  id: string
  name: string
  countries: number
  bonus: number
  color: string
}

export interface MapSettings {
  enableHover: boolean
  enableSelection: boolean
  showCountryInfo: boolean
  showTroopCounts: boolean
  showCountryNames: boolean
  animationSpeed: number
}

import type {Country} from "./country.interface"
