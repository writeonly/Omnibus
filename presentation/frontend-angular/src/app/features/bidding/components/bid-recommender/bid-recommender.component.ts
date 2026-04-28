import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { BidRecommendResponse } from '@core/models/bid.dto';

import { BidRecommenderService } from './bid-recommender.service';
import { BidFormState, System } from './bid-recommender.model';

@Component({
  selector: 'app-bid-recommender',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  providers: [BidRecommenderService],
  templateUrl: './bid-recommender.component.html',
})
export class BidRecommenderComponent {

  private readonly service = inject(BidRecommenderService);

  // UI state (ONLY UI)
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<BidRecommendResponse | null>(null);

  readonly form = new FormGroup({
    hand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  submit() {
    if (this.form.invalid) return;

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.service.recommendBid(this.form.getRawValue())
      .subscribe({
        next: (res) => {
          this.result.set(res);
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Request failed');
          this.loading.set(false);
        }
      });
  }

  reset() {
    this.form.reset(this.service.resetForm());

    this.result.set(null);
    this.error.set(null);
  }
}
