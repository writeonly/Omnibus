import { Injectable } from '@angular/core';
import { BffApiService } from '@core/api/bff/bff-api.service';
import { BiddingFormState } from './rest-bidding.model';

@Injectable()
export class RestBiddingService {

  constructor(private api: BffApiService) {}

  recommendBidding(payload: BiddingFormState) {
    return this.api.recommendBidding(payload);
  }

  resetForm(): BiddingFormState {
    return {
      northHand: '',
      southHand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    };
  }
}
