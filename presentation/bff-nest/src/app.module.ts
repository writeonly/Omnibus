import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './bidding/next-bid/next-bid.module';
import { RestBiddingModule } from './bidding/rest-bidding/rest-bidding.module';

@Module({
  imports: [NextBidModule, RestBiddingModule],
  controllers: [HealthController],
  providers: [],
})
export class AppModule {}