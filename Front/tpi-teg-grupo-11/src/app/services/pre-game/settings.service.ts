import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Settings} from '../../core/models/class/settings';
import {GameMap} from '../../core/models/class/game-map';
import {ObjectiveType} from '../../core/models/class/objective-type';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  API_SETTINGS_URL = 'http://localhost:8080/settings';
  API_GAMES_MAP_URL = 'http://localhost:8080/maps';
  API_OBJECTIVES_TYPES_URL = 'http://localhost:8080/objective-types';

  constructor(private http: HttpClient) { }

  public getAllGamesMap() {
    return this.http.get<GameMap[]>(this.API_GAMES_MAP_URL);
  }

  public getAllObjectivesTypes() {
    return this.http.get<ObjectiveType[]>(this.API_OBJECTIVES_TYPES_URL);
  }

  public createSettings(settings : Settings) {
    return this.http.post<Settings>(this.API_SETTINGS_URL, settings);
  }

  public findSettingsById(id : number) {
    return this.http.get<Settings>(`${this.API_SETTINGS_URL}/${id}`);
  }
}
