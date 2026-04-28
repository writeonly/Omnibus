import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { BiddingRecommendResponse } from '@core/models/bidding.dto';

import { BiddingRecommenderService } from './bidding-recommender.service';
import { BiddingFormState, System } from './bidding-recommender.model';

@Component({
  selector: 'app-bidding-recommender',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  providers: [BiddingRecommenderService],
  templateUrl: './bidding-recommender.component.html',
})
export class BiddingRecommenderComponent {

  private readonly service = inject(BiddingRecommenderService);

  // UI state only
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<BiddingRecommendResponse | null>(null);

  readonly form = new FormGroup({
    northHand: new FormControl<string>('', { nonNullable: true }),
    southHand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  submit() {
    if (this.form.invalid) return;

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.service.recommendBidding(this.form.getRawValue())
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