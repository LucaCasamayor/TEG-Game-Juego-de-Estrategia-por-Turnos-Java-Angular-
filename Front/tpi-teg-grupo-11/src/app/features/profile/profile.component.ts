import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';
import {User} from '../../core/models/class/user';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  user: User = JSON.parse(sessionStorage.getItem("user")!);

  isModalOpen = false;
  isEditing = false;
  isImageModalOpen = false;
  currentPassword: string = '';
  passwordError: boolean = false;
  showSuccessMessage = false;

  editedUser: Partial<User> = { imgUrl: this.user.imgUrl };

  availableImages: string[] = [
    'assets/images/team-members/Facundo Guzman.png',
    'assets/images/team-members/Gino Carabelli García.png',
    'assets/images/team-members/Gino Ceccarelli Enz.png',
    'assets/images/team-members/Leonardo Acosta Koenig.png',
    'assets/images/team-members/Luca Casamayor Porto.png',
    'assets/images/team-members/Mateo Delgado Costa.png'
  ];

  toggleModal() {
    this.isModalOpen = !this.isModalOpen;
    this.isEditing = false;
    this.editedUser = { imgUrl: this.user.imgUrl };
  }

  toggleEdit() {
    this.isEditing = true;
  }

  cancelEdit() {
    this.isEditing = false;
    this.editedUser = { imgUrl: this.user.imgUrl };
  }

  changePhoto() {
    this.isImageModalOpen = true;
  }

  selectImage(imgUrl: string) {
    this.editedUser.imgUrl = imgUrl;
    this.isImageModalOpen = false;
  }

  saveChanges() {
    if (this.editedUser.password && this.currentPassword !== this.user.password) {
      this.passwordError = true;
      return;
    }
    this.passwordError = false;

    // Actualizo sólo lo que se haya cambiado en editedUser
    this.user = { ...this.user, ...this.editedUser };

    // Guardar en sessionStorage
    sessionStorage.setItem('user', JSON.stringify(this.user));

    this.isEditing = false;
    this.currentPassword = '';
    this.editedUser.password = '';

    // Mostrar mensaje de éxito 3 segundos
    this.showSuccessMessage = true;
    setTimeout(() => this.showSuccessMessage = false, 3000);

    this.cancelEdit();
  }
}
