import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BffApiService } from '@core/api/bff-api.service';
import { BiddingRecommendResponse } from '@core/models/auction.dto';

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
export class BiddingRecommenderComponent {

  private readonly bffApi = inject(BffApiService);

  readonly form = signal<BiddingFormState>({ ...INITIAL_STATE });

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<BiddingRecommendResponse | null>(null);

  update<K extends keyof BiddingFormState>(key: K, value: BiddingFormState[K]) {
    this.form.update(state => ({
      ...state,
      [key]: value,
    }));
  }

  submit(): void {
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.bffApi.recommendBidding(this.form()).subscribe({
      next: (res) => {
        this.result.set(res);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to calculate bid');
        this.loading.set(false);
      }
    });
  }

  reset(): void {
    this.form.set({ ...INITIAL_STATE });
    this.result.set(null);
    this.error.set(null);
  }
}