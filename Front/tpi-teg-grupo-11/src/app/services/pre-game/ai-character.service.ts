import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AIProfile} from '../../core/enums/ai-profile';

export interface AICharacter {
  id: number;
  name: string;
  profile: AIProfile;
  description?: string;
  imgUrl?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AICharacterService {
  private API_URL = 'http://localhost:4200/ai-characters';

  constructor(private http: HttpClient) { }

  getAllCharacters(): Observable<AICharacter[]> {
    return this.http.get<AICharacter[]>(this.API_URL);
  }

  getCharactersByProfile(profile: AIProfile): Observable<AICharacter[]> {
    return this.http.get<AICharacter[]>(`${this.API_URL}/profile/${profile}`);
  }

  getCharacterById(id: number): Observable<AICharacter> {
    return this.http.get<AICharacter>(`${this.API_URL}/${id}`);
  }
}
