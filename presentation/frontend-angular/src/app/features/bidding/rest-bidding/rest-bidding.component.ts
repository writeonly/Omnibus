import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { RestBiddingResponse } from '@core/api/bff/dto/rest-bidding.dto';

import { RestBiddingService } from './rest-bidding.service';
import { BiddingFormState, System } from './rest-bidding.model';

@Component({
  selector: 'app-rest-bidding',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  providers: [RestBiddingService],
  templateUrl: './rest-bidding.component.html',
})
export class RestBiddingComponent {

  private readonly service = inject(RestBiddingService);

  // UI state only
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<RestBiddingResponse | null>(null);

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