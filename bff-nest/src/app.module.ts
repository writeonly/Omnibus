import { Module } from '@nestjs/common';
import { HealthController } from './health/health.controller';
import { BiddingController } from './bidding/bidding.controller';
import { BiddingService } from './bidding/bidding.service';
import { AuthController } from './auth/auth.controller';
import { AuthService } from './auth/auth.service';
import { AdminRulesController } from './admin/admin-rules.controller';
import { AdminRulesService } from './admin/admin-rules.service';

@Module({
  imports: [],
  controllers: [HealthController, AuthController, BiddingController, AdminRulesController],
  providers: [AuthService, BiddingService, AdminRulesService],
})
export class AppModule {}
