import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GamePlayersBarComponent} from './game-players-bar.component';

describe('GamePlayersBarComponent', () => {
  let component: GamePlayersBarComponent;
  let fixture: ComponentFixture<GamePlayersBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GamePlayersBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GamePlayersBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
