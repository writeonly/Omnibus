import { Injectable } from '@angular/core';
import { BffApiService } from '@core/api/bff/bff-api.service';
import { BidFormState } from './next-bid.model';

@Injectable()
export class NextBidService {

  constructor(private api: BffApiService) {}

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
