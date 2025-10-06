import {Component, ElementRef, ViewChild} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AboutUsComponent} from '../../shared/about-us/about-us.component';
import {MatIcon} from '@angular/material/icon';
import {NgIf} from '@angular/common';
import {ApiService} from '../../services/auth/api.service';

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    AboutUsComponent,
    MatIcon,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  storedUser = sessionStorage.getItem('user');
  session = this.storedUser ? JSON.parse(this.storedUser) : null;

  constructor(private apiService: ApiService, private router : Router) {
  }

  isModalOpen = false;

  toggleModal() {
    this.isModalOpen = !this.isModalOpen;
  }

  logOut() {
    sessionStorage.removeItem('user');
    this.router.navigateByUrl('/login');
  }
  openPdf() {
    window.open('/TEG_HowToPlay.pdf', '_blank');
  }

  @ViewChild('audioRef') audioRef!: ElementRef<HTMLAudioElement>;
  isPlaying = false;

  ngAfterViewInit() {
    const audio = this.audioRef.nativeElement;
    audio.volume = 0.5;

    audio.addEventListener('play', () => this.isPlaying = true);
    audio.addEventListener('pause', () => this.isPlaying = false);
  }

  togglePlayPause() {
    const audio = this.audioRef.nativeElement;
    if (audio.paused) {
      audio.play();
    } else {
      audio.pause();
    }
  }

}
