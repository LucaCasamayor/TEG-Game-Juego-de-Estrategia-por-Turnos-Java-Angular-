import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TerritoryState} from '../../core/models/class/game-data';

@Injectable({
  providedIn: 'root'
})
export class TerritoryStateService {
  API_URL = 'http://localhost:8080/territoryState';
  constructor(private http: HttpClient) { }

  getTerritoriesStatesByPlayer(playerId: number) {
    return this.http.get<TerritoryState>(`${this.API_URL}/player/${playerId}`);
  }
}
