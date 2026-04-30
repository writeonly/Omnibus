import { Module } from '@nestjs/common';
import { RestBiddingController } from './rest-bidding.controller';
import { RestBiddingService } from './rest-bidding.service';

@Module({
  controllers: [RestBiddingController],
  providers: [RestBiddingService],
  exports: [RestBiddingService],
})
export class RestBiddingModule {}