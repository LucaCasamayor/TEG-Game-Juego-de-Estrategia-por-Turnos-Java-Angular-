import {Directive, ElementRef, EventEmitter, OnInit, Output} from '@angular/core';
import {ApiService} from '../../../services/auth/api.service';

@Directive({
  selector: '[appSession]'
})
export class SessionDirective implements OnInit {
  @Output() sessionFound = new EventEmitter<any>();
  constructor(private el: ElementRef, private sessionService: ApiService) {}

  ngOnInit() {
    const session = this.sessionService.getUser();

    if (session) {
      this.sessionFound.emit(session);
    } else {
      this.sessionFound.emit(null);
    }
  }
}
