import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../core/models/class/user';
import {RecoveryPassword} from '../../core/models/class/recovery-password';
import {ResetPassword} from '../../core/models/class/reset-password';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private API_URL = 'http://localhost:8080/user';
  constructor(private http : HttpClient) { }

  public loginUser(username: string, password: string) {
    return this.http.post<User>(`${this.API_URL}/login`, { username, password });
  }
  public registerUser(user : User) {
    return this.http.post<User>(`${this.API_URL}/register`, user);
  }
  public resetUserPassword(id:number, newPassword: String, user : User) {
    return this.http.patch<User>(
      `${this.API_URL}/${id}`,
      { newPassword }
    );
  }
  public getUser() {
    return JSON.parse(sessionStorage.getItem("user")!);
  }

  recoverPassword(email: RecoveryPassword): Observable<void> {
    return this.http.post<void>(this.API_URL + "/recover-password", email);
  }

  resetPassword(resetDto: ResetPassword): Observable<void> {
    return this.http.post<void>(this.API_URL + "/reset-password", resetDto);
  }
}
