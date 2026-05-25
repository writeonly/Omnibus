import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { NextBidModule } from './api/rule/next-bid/next-bid.module';
import { RestBiddingModule } from './api/workflow/rest-bidding/rest-bidding.module';
import { AuthModule } from './api/auth/auth.module';
import { HttpClientsModule } from './api/client/http/http-clients.module';

@Module({
  imports: [
    AuthModule,
    HttpClientsModule,
    NextBidModule,
    RestBiddingModule
  ],
  controllers: [HealthController],
  providers: [],
})
export class AppModule { }
