import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { BffApiService } from '@core/api/bff-api.service';
import { BidRecommendResponse } from '@core/models/bid.dto';
import { FormsModule } from '@angular/forms';
import { BaseFeatureComponent } from '@shared/feature/base-feature.component';

type Seat = 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';
type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

interface BidFormState {
  hand: string;
  auction: string;
  seat: Seat;
  system: System;
}

const INITIAL_STATE: BidFormState = {
  hand: '',
  auction: '',
  seat: 'NORTH',
  system: 'POLISH_CLUB',
};

@Component({
  selector: 'app-bid-recommender',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bid-recommender.component.html',
})
export class BidRecommenderComponent extends BaseFeatureComponent<
  BidFormState,
  BidRecommendResponse
> {
  private readonly bffApi = inject(BffApiService);

  protected override getInitialForm(): BidFormState {
    return {
      hand: '',
      auction: '',
      seat: 'NORTH',
      system: 'POLISH_CLUB',
    };
  }

  protected override apiCall(payload: BidFormState) {
    return this.bffApi.recommendBid(payload);
  }
}