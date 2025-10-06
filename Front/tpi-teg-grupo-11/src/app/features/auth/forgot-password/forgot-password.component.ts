import {Component} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm, ReactiveFormsModule} from '@angular/forms';
import {ApiService} from '../../../services/auth/api.service';
import {RecoveryPassword} from '../../../core/models/class/recovery-password';
import {ResetPassword} from '../../../core/models/class/reset-password';

@Component({
  selector: 'app-forgot-password',
  imports: [
    RouterLink,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  data = {
    username: "",
    email: "",
    code: "",
    newPassword: "",
  };
  errorMessage: string = "";
  codigoEnviado = false;
  showPassword = false;

  constructor(private apiService: ApiService, private router: Router) {
  }

  onSubmit(form: NgForm): void {
    const dto: RecoveryPassword = {
      username: this.data.username,
      email: this.data.email
    };
    if (form.valid) {
      this.apiService.recoverPassword(dto)
        .subscribe({
          next: user => {
            alert("Codigo enviado");
            this.codigoEnviado = true;
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
      alert('Formulario inválido');
    }
  }

  onResetPassword(form: NgForm): void {
    const dto: ResetPassword = {
      username : this.data.username,
      code: this.data.code,
      newPassword: this.data.newPassword,
    };
    if (form.valid) {
      this.apiService.resetPassword(dto)
        .subscribe({
          next: user => {
            alert("Contraseña Actualizada");
            this.router.navigate(['/login']);
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
      alert('Formulario inválido');
    }
  }
}
