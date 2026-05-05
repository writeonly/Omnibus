import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './bidding/next-bid/next-bid.module';
import { RestBiddingModule } from './bidding/rest-bidding/rest-bidding.module';
import { OutboxModule } from './outbox/outbox.module';
import { GrpcClientsModule } from './common/grpc/grpc-clients.module';

@Module({
  imports: [OutboxModule, GrpcClientsModule, NextBidModule, RestBiddingModule],
  controllers: [HealthController],
  providers: [],
})
export class AppModule {}
