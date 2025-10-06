import {Component} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {FormsModule, NgForm} from '@angular/forms';
import {ApiService} from '../../../services/auth/api.service';
import {User} from '../../../core/models/class/user';

@Component({
  selector: 'app-register',
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  images = [
    "assets/images/team-members/Facundo Guzman.png",
    "assets/images/team-members/Mateo Delgado Costa.png",
    "assets/images/team-members/Luca Casamayor Porto.png",
    "assets/images/team-members/Leonardo Acosta Koenig.png",
    "assets/images/team-members/Gino Ceccarelli Enz.png",
    "assets/images/team-members/Gino Carabelli García.png",
  ]

  user: User = {
    username: "",
    password: "",
    imgUrl: this.images[Math.floor(Math.random() * 6)],
    active: true,
    creationDate: new Date()
  }

  errorMessage = "";
  confirmPassword: string = "";
  showPassword = false;
  showConfirmPassword = false;



  constructor(private apiService: ApiService, private router : Router) {
  }

  validateInputs(): boolean {
    const usernameLength = this.user.username.length;
    const passwordLength = this.user.password.length;

    return (
      usernameLength >= 5 && usernameLength <= 16 &&
      passwordLength >= 8 && passwordLength <= 16
    );
  }

  onSubmit(registerForm: NgForm) {
    if (!this.validateInputs()) {
      alert("El usuario debe tener entre 4 y 16 caracteres, y la contraseña entre 8 y 16.");
      return;
    }

    if (this.user.password !== this.confirmPassword) {
      alert("Las contraseñas no coinciden.");
      return;
    }

    if (registerForm.valid) {
      this.apiService.registerUser(this.user).subscribe({
        next: (user) => {
          alert("Registro Exitoso");
          sessionStorage.setItem("user", JSON.stringify(user));
          this.router.navigateByUrl('/');
        },
        error: error => {
          this.errorMessage = error.error.message;
        }
      });
    } else {
      console.log('Formulario inválido');
    }
  }


}
