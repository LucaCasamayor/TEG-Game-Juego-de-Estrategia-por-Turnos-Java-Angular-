export interface Country {
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
}

export interface CountryPath {
  id: string
  pathData: string
  countryId: string
  fill?: string
  stroke?: string
  strokeWidth?: number
}

export interface MapInteractionEvent {
  country: Country
  event: MouseEvent
  action: "click" | "hover" | "leave"
}

