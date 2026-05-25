import { Injectable } from '@nestjs/common';

import { HttpTransport } from './http-transport';

export interface BiddingRecommendationRequest {
  northHand: string;
  southHand: string;
  auction?: string;
  system: string;
}

export interface BiddingRecommendationResponse {
  recommendedBid: string;
  explanation: string;
  bidding: string;

  candidates: Array<{
    bid: string;
    priority: number;
    reason: string;
  }>;
}

@Injectable()
export class BiddingHttpClient {
  private readonly target =
    process.env.API_GATEWAY_HTTP_TARGET ??
    'http://localhost:8080';

  constructor(
    private readonly transport: HttpTransport
  ) {}

  async recommendBid(
    request: BiddingRecommendationRequest
  ): Promise<BiddingRecommendationResponse> {
    return this.transport.post(
      this.target,
      '/api/bidding/recommend-bid',
      request
    );
  }

  async saveManagedRule(
    name: string,
    content: string
  ): Promise<void> {
    await this.transport.post(
      this.target,
      '/api/bidding/managed-rules',
      {
        name,
        content,
      }
    );
  }
}
