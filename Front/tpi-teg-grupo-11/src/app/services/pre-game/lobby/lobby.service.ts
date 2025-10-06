import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Game} from '../../../core/models/class/game';
import {Player} from '../../../core/models/class/player';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {
  private API_URL = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  createLobbyGame(): Observable<Game> {
    return this.http.post<Game>(`${this.API_URL}/games`, {});
  }

  getGamePlayers(gameId: string): Observable<Player[]> {
    return this.http.get<Player[]>(`${this.API_URL}/games/${gameId}/players`);
  }

  addHumanPlayer(gameId: string, userId: number): Observable<Player> {
    const playerData = {
      gameId,
      userId,
      playerType: 'PLAYER'
    };
    return this.http.post<Player>(`${this.API_URL}/players`, playerData);
  }

  addBot(gameId: string, aiCharacterId: number): Observable<Player> {
    const playerData = {
      gameId,
      aiCharacterId,
      playerType: 'BOT'
    };
    return this.http.post<Player>(`${this.API_URL}/players`, playerData);
  }

  removePlayer(playerId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/players/${playerId}`);
  }

  startGame(gameId: string): Observable<Game> {
    return this.http.post<Game>(`${this.API_URL}/games/${gameId}/start`, {});
  }
}
