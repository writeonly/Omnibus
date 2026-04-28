import { Injectable } from '@angular/core';
import { BffApiService } from '@core/api/bff-api.service';
import { BidFormState } from './bid-recommender.model';

@Injectable()
export class BidRecommenderService {

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
