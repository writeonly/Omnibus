import { Body, Controller, Logger, Post } from '@nestjs/common';
import { NextBidRequestDtoDto } from './next-bid-request.dto';
import { NextBidService } from './next-bid.service';

@Controller('next-bid')
export class NextBidController {
  private readonly logger = new Logger(NextBidController.name);
  constructor(private readonly biddingService: NextBidService) { }

  @Post()
  recommend(@Body() request: NextBidRequestDtoDto) {
    this.logger.log('Received recommendation request');
    this.logger.debug(JSON.stringify(request));
    return this.biddingService.recommend(request);
  }
}
