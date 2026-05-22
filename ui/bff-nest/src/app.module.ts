import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './api/rule/next-bid/next-bid.module';
import { RestBiddingModule } from './api/workflow/rest-bidding/rest-bidding.module';
// import { OutboxModule } from './outbox/outbox.module';
import { GrpcClientsModule } from './api/common/grpc/grpc-clients.module';
import { AuthModule } from './api/auth/auth.module';

@Module({
  imports: [
    AuthModule,
    // OutboxModule,
    GrpcClientsModule,
    NextBidModule,
    RestBiddingModule
  ],
  controllers: [HealthController],
  providers: [],
})
export class AppModule { }
