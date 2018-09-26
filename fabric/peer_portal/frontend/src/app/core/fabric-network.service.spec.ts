import { TestBed, inject } from '@angular/core/testing';

import { FabricNetworkService } from './fabric-network.service';

describe('FabricNetworkService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FabricNetworkService]
    });
  });

  it('should be created', inject([FabricNetworkService], (service: FabricNetworkService) => {
    expect(service).toBeTruthy();
  }));
});
