import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './bidding/next-bid/next-bid.module';
import { RestBiddingModule } from './bidding/rest-bidding/rest-bidding.module';
import { OutboxModule } from './outbox/outbox.module';

@Module({
  imports: [OutboxModule, NextBidModule, RestBiddingModule],
  controllers: [HealthController],
  providers: [],
})
export class AppModule {}
