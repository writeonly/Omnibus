import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { BffApiService } from '@core/api/bff-api.service';
import { BidRecommendResponse } from '@core/models/bid.dto';
import { BaseFeatureComponent } from '@shared/feature/base-feature.component';
import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';

type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

interface BidFormState {
  hand: string;
  bidding: string;
  system: System;
}

@Component({
  selector: 'app-bid-recommender',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  templateUrl: './bid-recommender.component.html',
})
export class BidRecommenderComponent extends BaseFeatureComponent<
  BidRecommendResponse
> {
  private readonly bffApi = inject(BffApiService);

  // ✅ Reactive Form = single source of truth
  readonly form = new FormGroup({
    hand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  // ✅ API call
  protected override apiCall(payload: BidFormState) {
    return this.bffApi.recommendBid(payload);
  }

  // ✅ submit using Reactive Forms
  submit() {
    if (this.form.invalid) return;

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.apiCall(this.form.getRawValue())
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

  // ✅ reset form + state
  reset() {
    this.form.reset({
      hand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    });

    this.result.set(null);
    this.error.set(null);
  }
}
