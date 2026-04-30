import { Body, Controller, Logger, Post } from '@nestjs/common';
import { RestBiddingRequestDto } from './rest-bidding-request.dto';
import { RestBiddingService } from './rest-bidding.service';
import { RestBiddingResponseDto } from './rest-bidding-response.dto';

@Controller('rest-bidding')
export class RestBiddingController {
  private readonly logger = new Logger(RestBiddingController.name);
  constructor(private readonly biddingService: RestBiddingService) { }

  @Post()
  recommend(@Body() dto: RestBiddingRequestDto): RestBiddingResponseDto {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);
    return this.biddingService.recommend(dto);
  }
}
