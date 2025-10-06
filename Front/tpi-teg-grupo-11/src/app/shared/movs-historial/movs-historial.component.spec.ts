import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovsHistorialComponent } from './movs-historial.component';

describe('MovsHistorialComponent', () => {
  let component: MovsHistorialComponent;
  let fixture: ComponentFixture<MovsHistorialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovsHistorialComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MovsHistorialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
