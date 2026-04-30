import { Body, Controller, Logger, Post } from '@nestjs/common';
import { RestBiddingRequestDto } from './rest-bidding-request.dto';
import { RestBiddingService } from './rest-bidding.service';

@Controller('rest-bidding')
export class RestBiddingController {
  private readonly logger = new Logger(RestBiddingController.name);
  constructor(private readonly biddingService: RestBiddingService) { }

  @Post()
  recommend(@Body() request: RestBiddingRequestDto) {
    this.logger.log('Received recommendation request');
    this.logger.debug(JSON.stringify(request));
    return this.biddingService.recommend(request);
  }
}
