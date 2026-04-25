import { Body, Controller, Post } from '@nestjs/common';
import { RecommendationRequestDto } from './dto/recommendation-request.dto';
import { BiddingService } from './bidding.service';

@Controller('bidding')
export class BiddingController {
  constructor(private readonly biddingService: BiddingService) {}

  @Post('recommend')
  recommend(@Body() request: RecommendationRequestDto) {
    return this.biddingService.recommend(request);
  }
}
