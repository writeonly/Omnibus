import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  NextBidRequest,
  NextBidResponse,
} from '../dto/next-bid.dto';

import {
  RestBiddingRequest,
  RestBiddingResponse
} from '../dto/rest-bidding.dto';

@Injectable({ providedIn: 'root' })
export class BffApiClient {

  private readonly baseUrl = '/api';

  constructor(private http: HttpClient) { }

  recommendBid(req: NextBidRequest): Observable<NextBidResponse> {
    return this.http.post<NextBidResponse>(
      `${this.baseUrl}/next-bid`,
      req
    );
  }

  recommendBidding(req: RestBiddingRequest): Observable<RestBiddingResponse> {
    return this.http.post<RestBiddingResponse>(
      `${this.baseUrl}/rest-bidding`,
      req
    );
  }
}
