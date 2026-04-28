import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { BffApiService } from '@core/api/bff-api.service';
import { BaseFeatureComponent } from '@shared/feature/base-feature.component';
import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { BiddingRecommendResponse } from '@core/models/bidding.dto';

type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

interface BiddingFormState {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}

@Component({
  selector: 'app-bidding-recommender',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  templateUrl: './bidding-recommender.component.html',
})
export class BiddingRecommenderComponent extends BaseFeatureComponent<
  BiddingRecommendResponse
> {
  private readonly bffApi = inject(BffApiService);

  readonly form = new FormGroup({
    northHand: new FormControl<string>('', { nonNullable: true }),
    southHand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  protected override apiCall(payload: BiddingFormState) {
    return this.bffApi.recommendBidding(payload);
  }

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
      },
    });
  }

  reset() {
    this.form.reset({
      northHand: '',
      southHand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    });

    this.result.set(null);
    this.error.set(null);
  }
}
