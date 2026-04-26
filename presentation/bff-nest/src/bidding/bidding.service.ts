import { Injectable, BadGatewayException } from '@nestjs/common';
import { RecommendationRequestDto } from './dto/recommendation-request.dto';

type RecommendationResponse = {
  system: string;
  evaluatedSeat: string;
  northHand: string;
  southHand: string;
  auction: string;
  recommendedBid: string;
  explanation: string;
  candidates: Array<{ bid: string; priority: number; reason: string }>;
};

@Injectable()
export class BiddingService {
  private readonly backendBaseUrl =
    process.env.ENGINE_BASE_URL ?? 'http://localhost:8081';

  async recommend(
    request: RecommendationRequestDto,
  ): Promise<RecommendationResponse> {
    const payload = {
      northHand: request.northHand?.trim(),
      southHand: request.southHand?.trim(),
      auction: request.auction?.trim() ?? '',
      system: request.system?.trim() ?? 'DEFAULT_SYSTEM',
    };

    const response = await fetch(
      `${this.backendBaseUrl}/api/v1/bidding/recommend`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      },
    );

    if (!response.ok) {
      const body = await response.text();

      // lepszy kontekst błędu (zamiast "gołego throw")
      throw new BadGatewayException({
        message: 'Bidding backend error',
        status: response.status,
        details: body,
        payloadSent: payload,
      });
    }

    return (await response.json()) as RecommendationResponse;
  }
}
