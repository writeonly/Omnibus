import { Module } from '@nestjs/common';
import { NextBidController } from './next-bid.controller';
import { NextBidService } from './next-bid.service';

@Module({
  controllers: [NextBidController],
  providers: [NextBidService],
  exports: [NextBidService], // opcjonalnie, jeśli będzie używany gdzie indziej
})
export class NextBidModule {}