import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';

import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { NextBidResponse } from '@core/api/bff/dto/next-bid.dto';

import { NextBidService } from './next-bid.service';
import { System } from './next-bid.model';

@Component({
  selector: 'app-next-bid',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FeaturePanelComponent],
  providers: [NextBidService],
  templateUrl: './next-bid.component.html',
})
export class NextBidComponent {

  private readonly service = inject(NextBidService);

  // =========================
  // FORM (Reactive Forms)
  // =========================
  readonly form = new FormGroup({
    hand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  // =========================
  // UI STATE (Signals)
  // =========================
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<NextBidResponse | null>(null);

  // =========================
  // VIEW MODEL (UI aggregation)
  // =========================
  readonly vm = computed(() => ({
    loading: this.loading(),
    error: this.error(),
    result: this.result(),
    formValid: this.form.valid,
  }));

  // =========================
  // ACTIONS
  // =========================
  submit(): void {
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

  reset(): void {
    this.form.reset({
      hand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    });

    this.result.set(null);
    this.error.set(null);
  }
}
