import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgClass, NgFor} from '@angular/common';

@Component({
  selector: 'app-about-us',
  standalone: true,
  imports: [
    NgClass, NgFor
  ],
  templateUrl: './about-us.component.html',
  styleUrl: './about-us.component.css'
})
export class AboutUsComponent {
  @Input() isModalOpen: boolean = false;
  @Output() closeModal = new EventEmitter<void>();

  team = [
    {
      name: 'Luca Casamayor Porto',
      image: 'assets/images/team-members/Luca Casamayor Porto.png',
      description: 'Legajo: 412084'
    },
    {
      name: 'Gino Carabelli García',
      image: 'assets/images/team-members/Gino Carabelli García.png',
      description: 'Legajo: 412045'
    },
    {
      name: 'Leonardo Acosta Koenig',
      image: 'assets/images/team-members/Leonardo Acosta Koenig.png',
      description: 'Legajo: 412194'
    },
    {
      name: 'Gino Ceccarelli Enz',
      image: 'assets/images/team-members/Gino Ceccarelli Enz.png',
      description: 'Legajo: 412704'
    },
    {
      name: 'Facundo Guzman',
      image: 'assets/images/team-members/Facundo Guzman.png',
      description: 'Legajo: 405091'
    },
    {
      name: 'Mateo Delgado Costa',
      image: 'assets/images/team-members/Mateo Delgado Costa.png',
      description: 'Legajo: 416087'
    },
  ];

  onClose() {
    this.closeModal.emit(); // esto notificará al padre
  }
}


