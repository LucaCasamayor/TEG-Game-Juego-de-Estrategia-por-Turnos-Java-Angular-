import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { GameService} from '../../services/game/game.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  standalone: true,
  imports: [
    FormsModule,
    RouterLink
  ]
})
export class JoinGameComponent {
  isPrivate = false;
  gameId = '';
  password = '';
  errorMessage: string | null = null;

  constructor(private gameService: GameService, private router: Router) {}

  joinGame() {
    const userData = sessionStorage.getItem('user');
    let userId: string | null = null;

    if (userData) {
      try {
        const userObj = JSON.parse(userData);
        userId = userObj.userId;
      } catch (e) {
        console.error('Error parseando user JSON', e);
      }
    }

    if (!userId) {
      this.errorMessage = 'Usuario no autenticado';
      return;
    }
    if (!this.gameId) {
      this.errorMessage = 'Debes ingresar el ID de la partida';
      return;
    }
    if (this.isPrivate && !this.password) {
      this.errorMessage = 'Debes ingresar la contraseña';
      return;
    }

    this.gameService.joinGame(this.gameId, userId, this.isPrivate ? this.password : '').subscribe({
      next: () => {
        this.errorMessage = null;
        this.router.navigate(['/lobby', this.gameId]);
      },
      error: err => {
        console.error('Error al unirse:', err);
        this.errorMessage = err.error?.message || 'Error al unirse a la partida';
      }
    });
  }
}
