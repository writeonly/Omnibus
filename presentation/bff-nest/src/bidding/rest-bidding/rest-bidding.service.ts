import { Injectable, Logger } from '@nestjs/common';
import { RestBiddingRequestDto } from './rest-bidding-request.dto';
import { RestBiddingResponseDto } from './rest-bidding-response.dto';

@Injectable()
export class RestBiddingService {
  private readonly logger = new Logger(RestBiddingService.name);

  recommend(dto: RestBiddingRequestDto): RestBiddingResponseDto {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);

    return {
      bidding: 'pass pass'
    }
  }
}
