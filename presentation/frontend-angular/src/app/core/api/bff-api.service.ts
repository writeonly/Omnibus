import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  BidRecommendRequest,
  BidRecommendResponse,
} from '../models/bid.dto';

import {
  AuctionAnalyzeRequest,
  AuctionAnalyzeResponse
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

  analyzeAuction(req: AuctionAnalyzeRequest): Observable<AuctionAnalyzeResponse> {
    return this.http.post<AuctionAnalyzeResponse>(
      `${this.baseUrl}/auction/analyze`,
      req
    );
  }
}
