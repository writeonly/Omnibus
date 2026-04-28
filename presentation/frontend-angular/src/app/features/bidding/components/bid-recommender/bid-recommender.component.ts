import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { BffApiService } from '@core/api/bff-api.service';
import { BidRecommendResponse } from '@core/models/bid.dto';

type BidFormValue = {
  hand: string;
  auction: string;
  seat: 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';
  system: string;
};

@Component({
  selector: 'app-bid-recommender',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './bid-recommender.component.html',
})
export class BidRecommenderComponent {

  form: FormGroup;
  result: BidRecommendResponse | null = null;
  
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private bffApi: BffApiService
  ) {
    this.form = this.fb.group({
      hand: [''],
      auction: [''],
      seat: ['NORTH'],
      system: ['SAYC'],
    });
  }

  submit(): void {
    this.loading = true;
    this.error = null;
    this.result = null;

    const payload: BidFormValue = this.form.getRawValue();

    this.bffApi.recommendBid(payload).subscribe({
      next: (response: BidRecommendResponse) => {
        this.result = response;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to calculate bid';
        this.loading = false;
      },
    });
  }

  reset(): void {
    this.form.reset({
      hand: '',
      auction: '',
      seat: 'NORTH',
      system: 'SAYC',
    });

    this.result = null;
    this.error = null;
  }
}