import { Injectable } from '@angular/core';
import { BffApiService } from '@core/api/bff-api.service';
import { BiddingFormState } from './bidding-recommender.model';

@Injectable()
export class BiddingRecommenderService {

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
