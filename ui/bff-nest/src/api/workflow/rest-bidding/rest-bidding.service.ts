import { Injectable, Logger } from '@nestjs/common';
import { RestBiddingRequestDto } from './rest-bidding-request.dto';
import { RestBiddingResponseDto } from './rest-bidding-response.dto';
import { BiddingGrpcClient } from '../../common/grpc/bidding-grpc.client';

@Injectable()
export class RestBiddingService {
  private readonly logger = new Logger(RestBiddingService.name);

  constructor(private readonly biddingGrpcClient: BiddingGrpcClient) {}

  async recommend(dto: RestBiddingRequestDto): Promise<RestBiddingResponseDto> {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);

    const recommendation = await this.biddingGrpcClient.recommendBid({
      northHand: dto.northHand,
      southHand: dto.southHand,
      auction: dto.bidding,
      system: dto.system,
    });

    return {
      bidding: recommendation.recommendedBid,
      explanation: recommendation.explanation,
    };
  }
}
