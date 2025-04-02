import { TestBed } from '@angular/core/testing';

import { RequisitionsService } from './requisitions.service';

describe('RequisitionsService', () => {
  let service: RequisitionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RequisitionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
