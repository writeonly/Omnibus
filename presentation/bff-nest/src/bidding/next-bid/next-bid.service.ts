import { Injectable, Logger } from '@nestjs/common';
import { NextBidRequestDto } from './next-bid-request.dto';
import { NextBidResponseDto } from './next-bid-response.dto';
import { BiddingGrpcClient } from '../../common/grpc/bidding-grpc.client';

@Injectable()
export class NextBidService {
  private readonly logger = new Logger(NextBidService.name);
  private readonly placeholderSouthHand =
    process.env.NEXT_BID_PLACEHOLDER_SOUTH_HAND ?? 'T97 A854 Q76 K98';

  constructor(private readonly biddingGrpcClient: BiddingGrpcClient) {}

  async recommend(dto: NextBidRequestDto): Promise<NextBidResponseDto> {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);

    const recommendation = await this.biddingGrpcClient.recommendBid({
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
