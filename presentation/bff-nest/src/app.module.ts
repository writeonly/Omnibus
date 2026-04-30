import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './bidding/next-bid/next-bid.module';

@Module({
  imports: [NextBidModule],
  controllers: [HealthController],
  providers: [],
})
export class AppModule {}