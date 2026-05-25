import { Injectable, Logger } from '@nestjs/common';

import { NextBidRequestDto } from './next-bid-request.dto';
import { NextBidResponseDto } from './next-bid-response.dto';

import { BiddingHttpClient } from '../../client/http/bidding-http.client';

@Injectable()
export class NextBidService {
  private readonly logger = new Logger(NextBidService.name);

  private readonly placeholderSouthHand =
    process.env.NEXT_BID_PLACEHOLDER_SOUTH_HAND ??
    'T97 A854 Q76 K98';

  constructor(
    private readonly biddingHttpClient: BiddingHttpClient
  ) {}

  async recommend(
    dto: NextBidRequestDto
  ): Promise<NextBidResponseDto> {
    this.logger.log(
      `Input received: ${JSON.stringify(dto)}`
    );

    const recommendation =
      await this.biddingHttpClient.recommendBid({
        northHand: dto.hand,
        southHand: this.placeholderSouthHand,
        auction: dto.bidding,
        system: dto.system,
      });

    return {
      bid: recommendation.recommendedBid,
      explanation: recommendation.explanation,
    };
  }
}
