import {TestBed} from '@angular/core/testing';

import {AICharacterService} from './ai-character.service';

describe('AiCharacterService', () => {
  let service: AICharacterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AICharacterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
