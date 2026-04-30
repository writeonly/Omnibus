import { Injectable, Logger } from '@nestjs/common';
import { NextBidRequestDto } from './next-bid-request.dto';
import { NextBidResponseDto } from './next-bid-response.dto';

@Injectable()
export class NextBidService {
  private readonly logger = new Logger(NextBidService.name);

  recommend(dto: NextBidRequestDto): NextBidResponseDto {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);

    return {
      bid: 'pass',
    };
  }
}