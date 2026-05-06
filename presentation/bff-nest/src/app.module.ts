import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './bidding/next-bid/next-bid.module';
import { RestBiddingModule } from './bidding/rest-bidding/rest-bidding.module';
import { OutboxModule } from './outbox/outbox.module';
import { GrpcClientsModule } from './common/grpc/grpc-clients.module';
import { AuthModule } from './auth/auth.module';

@Module({
  imports: [
    AuthModule,
    OutboxModule,
    GrpcClientsModule,
    NextBidModule,
    RestBiddingModule
  ],
  controllers: [HealthController],
  providers: [],
})
export class AppModule { }
