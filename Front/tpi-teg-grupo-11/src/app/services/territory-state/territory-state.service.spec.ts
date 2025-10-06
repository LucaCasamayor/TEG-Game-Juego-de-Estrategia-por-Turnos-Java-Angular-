import {TestBed} from '@angular/core/testing';

import {TerritoryStateService} from './territory-state.service';

describe('TerritoryStateService', () => {
  let service: TerritoryStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TerritoryStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
