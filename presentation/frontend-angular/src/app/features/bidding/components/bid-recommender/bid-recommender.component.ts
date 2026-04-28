import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { BffApiService } from '@core/api/bff-api.service';
import { BidRecommendResponse } from '@core/models/bid.dto';
import { FormsModule } from '@angular/forms';

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
export class BidRecommenderComponent {
  private readonly bffApi = inject(BffApiService);

  // UI state
  readonly form = signal<BidFormState>({ ...INITIAL_STATE });
  readonly result = signal<BidRecommendResponse | null>(null);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  // --- actions ---
  update<K extends keyof BidFormState>(key: K, value: BidFormState[K]) {
    this.form.update(current => ({
      ...current,
      [key]: value,
    }));
  }

  submit(): void {
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    const payload = this.form();

    this.bffApi.recommendBid(payload).subscribe({
      next: (res) => {
        this.result.set(res);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to calculate bid');
        this.loading.set(false);
      },
    });
  }

  reset(): void {
    this.form.set({ ...INITIAL_STATE });
    this.result.set(null);
    this.error.set(null);
  }
}
