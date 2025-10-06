import {Component} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import {ApiService} from '../../../services/auth/api.service';

@Component({
  selector: 'app-login',
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  data = {
    username: "",
    password: ""
  };

  errorMessage: string = "";
  showPassword = false;


  constructor(private apiService: ApiService, private router: Router) {
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      this.apiService.loginUser(this.data.username, this.data.password)
        .subscribe({
          next: user => {
            console.log("Login exitoso");
            sessionStorage.setItem("user", JSON.stringify(user));
            this.router.navigateByUrl('/');
          },
          error: error => {
            if (error.status === 404) {
              this.errorMessage = 'Credenciales incorrectas.';
            } else {
              this.errorMessage = 'Ocurrió un error inesperado.';
            }
          }
        });
    } else {
      console.log('Formulario inválido');
    }
  }
}
