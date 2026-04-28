
import { Component, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { FeaturePanelComponent } from '@shared/ui/feature-panel/feature-panel.component';
import { RestBiddingService } from './rest-bidding.service';
import { RestBiddingResponse } from '@core/api/bff/dto/rest-bidding.dto';
import { System } from './rest-bidding.model';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

@Component({
    selector: 'app-rest-bidding',
    imports: [
      ReactiveFormsModule,  
      ReactiveFormsModule,
      MatCardModule,
      MatFormFieldModule,
      MatInputModule,
      MatSelectModule,
      MatButtonModule],
    providers: [RestBiddingService],
    templateUrl: './rest-bidding.component.html'
})
export class RestBiddingComponent {

  private readonly service = inject(RestBiddingService);
  private readonly destroyRef = inject(DestroyRef);

  // =========================
  // FORM
  // =========================
  readonly form = new FormGroup({
    northHand: new FormControl<string>('', { nonNullable: true }),
    southHand: new FormControl<string>('', { nonNullable: true }),
    bidding: new FormControl<string>('', { nonNullable: true }),
    system: new FormControl<System>('POLISH_CLUB', { nonNullable: true }),
  });

  // =========================
  // UI STATE
  // =========================
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<RestBiddingResponse | null>(null);

  // =========================
  // VIEW MODEL
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

    this.service.recommendBidding(this.form.getRawValue())
      .pipe(takeUntilDestroyed(this.destroyRef))
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
      northHand: '',
      southHand: '',
      bidding: '',
      system: 'POLISH_CLUB',
    });

    this.result.set(null);
    this.error.set(null);
  }
}
