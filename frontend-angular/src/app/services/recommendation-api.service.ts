import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

export type RecommendationResponse = {
  system: string;
  hand: string;
  auction: string;
  recommendedBid: string;
  explanation: string;
  candidates: Array<{ bid: string; priority: number; reason: string }>;
};

type RecommendationRequest = {
  hand: string;
  auction: string;
  system: string;
};

@Injectable({
  providedIn: 'root',
})
export class RecommendationApiService {
  private readonly httpClient = inject(HttpClient);

  recommend(request: RecommendationRequest): Observable<RecommendationResponse> {
    return this.httpClient
      .post<RecommendationResponse>('http://localhost:3000/api/bidding/recommend', request)
      .pipe(
        catchError((error) =>
          throwError(() => new Error(error?.error?.message ?? 'Unable to reach BFF')),
        ),
      );
  }
}
