import { Injectable } from '@nestjs/common';
import { RecommendationRequestDto } from './dto/recommendation-request.dto';

type RecommendationResponse = {
  system: string;
  hand: string;
  auction: string;
  recommendedBid: string;
  explanation: string;
  candidates: Array<{ bid: string; priority: number; reason: string }>;
};

@Injectable()
export class BiddingService {
  private readonly backendBaseUrl =
    process.env.BACKEND_BASE_URL ?? 'http://localhost:8080';

  async recommend(
    request: RecommendationRequestDto,
  ): Promise<RecommendationResponse> {
    const response = await fetch(
      `${this.backendBaseUrl}/api/v1/bidding/recommend`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          hand: request.hand,
          auction: request.auction ?? '',
          system: request.system,
        }),
      },
    );

    if (!response.ok) {
      const body = await response.text();
      throw new Error(`Bidding backend returned ${response.status}: ${body}`);
    }

    return (await response.json()) as RecommendationResponse;
  }
}
