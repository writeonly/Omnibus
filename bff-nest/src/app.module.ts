import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { BiddingController } from './bidding/bidding.controller';
import { BiddingService } from './bidding/bidding.service';

@Module({
  imports: [],
  controllers: [HealthController, BiddingController],
  providers: [BiddingService],
})
export class AppModule {}
