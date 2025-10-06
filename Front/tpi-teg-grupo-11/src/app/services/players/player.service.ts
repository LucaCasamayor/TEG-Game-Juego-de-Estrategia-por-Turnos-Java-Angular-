import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, Subject, tap} from 'rxjs';
import {Player} from '../../core/models/class/player';
import {GameData} from '../../core/models/class/game-data';

export interface AttackRequestDto {
  from: number;
  to: number;
  armyCount: number;
}

export interface DeployRequestDto {
  armyCount: number;
  territory: number;
}

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private readonly API_URL = 'http://localhost:8080/player';
  private readonly API_BOT_URL = 'http://localhost:8080/bot';
  constructor(private http: HttpClient) {}

  private playerSubject = new Subject<Player>();
  public player$ = this.playerSubject.asObservable();

  attack(from: number, to: number, armyCount: number): Observable<void> {
    const body: AttackRequestDto = { from, to, armyCount };
    return this.http.post<void>(`${this.API_URL}/attack`, body);
  }

  deploy(armyCount: number, territory: number): Observable<void> {
    const body: DeployRequestDto = { armyCount, territory };
    return this.http.post<void>(`${this.API_URL}/deploy`, body);
  }

  fortify(from: number, to: number, armyCount: number): Observable<void> {
    const body: AttackRequestDto = { from, to, armyCount };
    return this.http.post<void>(`${this.API_URL}/fortify`, body);
  }

  createPlayer(player : Player) {
    return this.http.post<Player>(this.API_URL, player);
  }

  createBotPlayer(player : Player) {
    return this.http.post<Player>(this.API_BOT_URL, player).pipe(
      tap((player) => this.playerSubject.next(player))
    );
  }

  getPlayersByGame(gameId : String) {
    return this.http.get<Player[]>(`${this.API_URL}/game/${gameId}`)
  }

  getById(playerId: number) {
    return this.http.get<Player[]>(`${this.API_URL}/game/${playerId}`)
  }
}
