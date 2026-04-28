import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BffApiService } from '@core/api/bff-api.service';
import { BiddingRecommendResponse } from '@core/models/auction.dto';
import { BaseFeatureComponent } from '@shared/feature/base-feature.component';

type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

interface BiddingFormState {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}

const INITIAL_STATE: BiddingFormState = {
  northHand: '',
  southHand: '',
  bidding: '',
  system: 'POLISH_CLUB',
};

@Component({
  selector: 'app-bidding-recommender',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bidding-recommender.component.html',
})
export class BiddingRecommenderComponent extends BaseFeatureComponent<
  BiddingFormState,
  BiddingRecommendResponse
> {

  private readonly bffApi = inject(BffApiService);

  protected override getInitialForm(): BiddingFormState {
    return {
      northHand: '',
      southHand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    };
  }

  protected override apiCall(payload: BiddingFormState) {
    return this.bffApi.recommendBidding(payload);
  }
}
