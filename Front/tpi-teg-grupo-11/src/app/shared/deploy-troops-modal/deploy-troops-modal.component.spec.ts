import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeployTroopsModalComponent} from './deploy-troops-modal.component';

describe('DeployTroopsModalComponent', () => {
  let component: DeployTroopsModalComponent;
  let fixture: ComponentFixture<DeployTroopsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeployTroopsModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeployTroopsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
