import type {AIProfile} from "../../enums/ai-profile"
import type {Color} from "../../enums/color"

export interface AICharacter {
  id: number
  name: string
  profile: AIProfile
  description?: string
  imgUrl?: string
  // Propiedades adicionales para el lobby
  playerColor?: Color
  turnOrder?: number
}
