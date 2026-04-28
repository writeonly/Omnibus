import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  BidRecommendRequest,
  BidRecommendResponse,
} from '../models/bid.dto';

import {
  BiddingRecommendRequest,
  BiddingRecommendResponse
} from '../models/auction.dto';

@Injectable({ providedIn: 'root' })
export class BffApiService {

  private readonly baseUrl = '/api';

  constructor(private http: HttpClient) {}

  recommendBid(req: BidRecommendRequest): Observable<BidRecommendResponse> {
    return this.http.post<BidRecommendResponse>(
      `${this.baseUrl}/bid/recommend`,
      req
    );
  }

  recommendBidding(req: BiddingRecommendRequest): Observable<BiddingRecommendResponse> {
    return this.http.post<BiddingRecommendResponse>(
      `${this.baseUrl}/auction/analyze`,
      req
    );
  }
}
