import { Component, computed, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { RestBiddingService } from './rest-bidding.service';
import { RestBiddingResponse } from '@core/api/dto/rest-bidding.dto';
import { System } from './rest-bidding.model';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-rest-bidding',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatExpansionModule
  ],
  providers: [RestBiddingService],
  templateUrl: './rest-bidding.component.html'
})
export class RestBiddingComponent {

  private readonly service = inject(RestBiddingService);
  private readonly destroyRef = inject(DestroyRef);

  // =========================
  // FORM (VALIDATION ADDED)
  // =========================
  readonly form = new FormGroup({
    northHand: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    southHand: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    bidding: new FormControl<string>('', {
      nonNullable: true,
    }),
    system: new FormControl<System>('POLISH_CLUB', {
      nonNullable: true,
    }),
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
    if (this.form.invalid) {
      this.form.markAllAsTouched(); // 👈 UX: pokaż błędy od razu
      return;
    }

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
