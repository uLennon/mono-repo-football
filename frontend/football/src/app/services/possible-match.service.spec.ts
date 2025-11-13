import { TestBed } from '@angular/core/testing';

import { PossibleMatchService } from './possible-match.service';

describe('PossibleMatchService', () => {
  let service: PossibleMatchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PossibleMatchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
