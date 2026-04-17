import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecommendationApiService, RecommendationResponse } from './services/recommendation-api.service';
import { RecommendationResultComponent } from './components/recommendation-result/recommendation-result.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, RecommendationResultComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  readonly hand = signal('AQJ KQ2 A43 J742');
  readonly auction = signal('');
  readonly system = signal('POLISH_CLUB');
  readonly loading = signal(false);
  readonly error = signal('');
  readonly result = signal<RecommendationResponse | null>(null);

  constructor(private readonly recommendationApiService: RecommendationApiService) {}

  submit(): void {
    this.loading.set(true);
    this.error.set('');

    this.recommendationApiService
      .recommend({
        hand: this.hand(),
        auction: this.auction(),
        system: this.system(),
      })
      .subscribe({
        next: (response) => {
          this.result.set(response);
          this.loading.set(false);
        },
        error: (error: Error) => {
          this.error.set(error.message);
          this.loading.set(false);
        },
      });
  }
}
