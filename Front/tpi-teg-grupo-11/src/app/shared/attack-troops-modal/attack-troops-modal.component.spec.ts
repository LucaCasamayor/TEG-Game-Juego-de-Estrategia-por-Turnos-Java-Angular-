import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AttackTroopsModalComponent} from './attack-troops-modal.component';

describe('AttackTroopsModalComponent', () => {
  let component: AttackTroopsModalComponent;
  let fixture: ComponentFixture<AttackTroopsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttackTroopsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AttackTroopsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
