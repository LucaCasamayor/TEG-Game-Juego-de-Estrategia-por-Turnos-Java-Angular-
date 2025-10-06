import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiInGameService {
  private API_URL = 'http://localhost:8080/';
  constructor(private http : HttpClient) { }
}
