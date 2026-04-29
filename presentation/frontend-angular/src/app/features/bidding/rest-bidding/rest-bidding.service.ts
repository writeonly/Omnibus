import { Injectable } from '@angular/core';
import { BffApiClient } from '@core/api/bff/client/bff-api.client';
import { BiddingFormState } from './rest-bidding.model';

@Injectable()
export class RestBiddingService {

  constructor(private api: BffApiClient) { }

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
