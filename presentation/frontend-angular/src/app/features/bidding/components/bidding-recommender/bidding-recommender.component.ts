import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BffApiService } from '@core/api/bff-api.service';
import { BiddingRecommendResponse } from '@core/models/auction.dto';


@Component({
  selector: 'app-bidding-recommender',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bidding-recommender.component.html',
})
export class BiddingRecommenderComponent {

  northHand = signal('');
  southHand = signal('');
  bidding = signal('');
  system = signal('POLISH_CLUB');

  loading = signal(false);
  error = signal<string | null>(null);
  result = signal<BiddingRecommendResponse | null>(null);
  

  constructor(private bffApi: BffApiService) {}

  submit(): void {
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    const payload = {
      northHand: this.northHand(),
      southHand: this.southHand(),
      bidding: this.bidding(),
      system: this.system(),
    };

    this.bffApi.recommendBidding(payload).subscribe({
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
    this.northHand.set('');
    this.southHand.set('');
    this.bidding.set('');
    this.system.set('POLISH_CLUB');

    this.result.set(null);
    this.error.set(null);
  }
}