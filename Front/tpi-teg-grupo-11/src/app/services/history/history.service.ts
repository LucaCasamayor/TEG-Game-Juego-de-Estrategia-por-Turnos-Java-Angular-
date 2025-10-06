import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Territory } from '../../core/models/class/game-data';
import { Country } from '../../core/models/interfaces/country.interface';
import {Movement} from "../../core/models/class/movement";
import { MovementDisplay } from '../../core/models/class/movement-display';



@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  territory : Territory | undefined;

  private readonly baseUrl = `http://localhost:8080/territories`;

  constructor(private http: HttpClient) { }

  getAllByMapId(mapId: number) { //trae todos los territorios
    return this.http.get<Territory[]>(`${this.baseUrl}/map/${mapId}`);
  }

  getMovementsDisplayByGame(gameId: string)  {
    return this.http.get<MovementDisplay[]>(`http://localhost:8080/movements/game/${gameId}`);
  }

  getAllByRegionId(regionId: number) {
    return this.http.get<Territory[]>(`${this.baseUrl}/region/${regionId}`);
  }

  getById(territoryId: number){
    return this.http.get<Territory>(`${this.baseUrl}/${territoryId}`);
  }
}
