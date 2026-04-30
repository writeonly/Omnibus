import { Body, Controller, Logger, Post } from '@nestjs/common';
import { NextBidService } from './next-bid.service';
import { NextBidRequestDto } from './next-bid-request.dto';
import { NextBidResponseDto } from './next-bid-response.dto';

@Controller('next-bid')
export class NextBidController {
  private readonly logger = new Logger(NextBidController.name);
  constructor(private readonly service: NextBidService) {}

  @Post()
  recommend(@Body() dto: NextBidRequestDto): NextBidResponseDto {
    this.logger.log(`Input received: ${JSON.stringify(dto)}`);
    return this.service.recommend(dto);
  }
}