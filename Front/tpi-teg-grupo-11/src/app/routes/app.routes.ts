import {Routes} from '@angular/router';
import {HomeComponent} from '../features/home/home.component';
import {LoginComponent} from '../features/auth/login/login.component';
import {RegisterComponent} from '../features/auth/register/register.component';
import {authGuard} from '../guard/auth.guard';
import {JoinGameComponent} from '../features/join-game/join-game.component'
import {NotFoundComponent} from '../features/not-found/not-found.component';
import {FAQsComponent} from '../features/faqs/faqs.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'join-game', component: JoinGameComponent },
  { path: 'faqs', component: FAQsComponent},
  {
    path: 'forgot-password',
    loadComponent: () =>
      import('../features/auth/forgot-password/forgot-password.component').then(
        m => m.ForgotPasswordComponent
      ),
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('../features/profile/profile.component').then(
        p => p.ProfileComponent
      ),
    canActivate: [authGuard]
  },
  {
    path: 'match-settings',
    loadComponent: () =>
      import('../features/match-settings/match-settings.component').then(
        m => m.MatchSettingsComponent
      )
  },
  {
    path: 'lobby/:id',
    loadComponent: () =>
      import('../features/lobby/lobby.component').then(
        m => m.LobbyComponent
      )
  },
  {
    path: 'game/:id',
    loadComponent: () =>
      import('../features/game/game.component').then(
        m => m.GameComponent
      )
  },

  {
    path: 'map-test',
    loadComponent: () =>
      import('../features/map/map.component').then(
        m => m.MapComponent
      )
  },
  {
    path: '**',
    component: NotFoundComponent,
  }

];
