import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {GameMap} from '../../core/models/class/game-map';
import {ObjectiveType} from '../../core/models/class/objective-type';
import {Settings} from '../../core/models/class/settings';
import {Game} from '../../core/models/class/game';
import {Player} from '../../core/models/class/player';
import {Observable, Subject, tap} from 'rxjs';
import {GameData} from '../../core/models/class/game-data';
import {GameState} from '../../core/enums/game-state';
import { Movement } from '../../core/models/class/movement';
import {Turn} from "../../core/models/class/turn";

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private API_URL = "http://localhost:8080/games"
  private gameSubject = new Subject<GameData>();
  public game$ = this.gameSubject.asObservable();

  constructor(private http : HttpClient) { }

  public getGameDataById(id : string) {
    return this.http.get<GameData>(`${this.API_URL}/${id}`);
  }

  getMovementsByGame(gameId: string)  {
    return this.http.get<Movement[]>(`http://localhost:8080/movements/game/${gameId}`);
  }

  getTurnsByGame(gameId: string) {
    return this.http.get<Turn[]>(`http://localhost:8080/turnos/game/${gameId}`);
  }


  public createGame(game : Game) {
    return this.http.post<Game>(this.API_URL, game);
  }

  public updateGameState(id: string, gameState: GameState) {
    console.log(JSON.stringify({gameState}));
    return this.http.put<Game>(`${this.API_URL}/state/${id}`, {gameState});
  }

  updateGameInGame(id: string, game: GameData) {
    return this.http.put<GameData>(`${this.API_URL}/in-game/${id}`, game).pipe(
      tap((game) => this.gameSubject.next(game)),
    );
  }

  updateGame(id: string, game: Game) {
    return this.http.put<Game>(`${this.API_URL}/${id}`, game);
  }

  getGameById(gameId: string) {
    return this.http.get<Game>(`${this.API_URL}/lobby/${gameId}`);
  }

  joinGame(gameId: string, userId: string, password?: string): Observable<GameData> {
    console.log('API_URL:', this.API_URL);
    const url = `${this.API_URL}/join/${gameId}`;
    const body = { userId, password };
    return this.http.put<GameData>(url, body).pipe(
      tap((game) => this.gameSubject.next(game)),
    );
  }

  getCurrentTurn(gameId: string) {
    return this.http.get<Turn>(`${this.API_URL}/turn/${gameId}`);
  }

  didPlayerConquer(turnId: number, playerId: number) {
    return this.http.get<Turn>(`${this.API_URL}/${turnId}/${playerId}`);
  }

    updateGamePhase(gameId: string, game: GameData) {
        return this.http.put<GameData>(`${this.API_URL}/in-game-phase/${gameId}`, game).pipe(
            tap((game) => this.gameSubject.next(game)),
        );
    }

}
