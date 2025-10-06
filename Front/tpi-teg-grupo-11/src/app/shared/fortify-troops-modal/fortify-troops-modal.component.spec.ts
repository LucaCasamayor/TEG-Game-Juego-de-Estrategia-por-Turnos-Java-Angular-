import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FortifyTroopsModalComponent } from './fortify-troops-modal.component';

describe('FortifyTroopsModalComponent', () => {
  let component: FortifyTroopsModalComponent;
  let fixture: ComponentFixture<FortifyTroopsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FortifyTroopsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FortifyTroopsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
