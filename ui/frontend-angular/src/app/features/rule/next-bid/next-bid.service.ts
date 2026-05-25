import { Injectable } from '@angular/core';
import { BffApiClient } from '@core/api/bff-api.client';
import { BidFormState } from './next-bid.model';

@Injectable()
export class NextBidService {

  constructor(private api: BffApiClient) { }

  recommendBid(payload: BidFormState) {
    return this.api.recommendBid(payload);
  }

  resetForm(): BidFormState {
    return {
      hand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    };
  }
}
