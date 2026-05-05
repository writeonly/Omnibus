import { Module } from '@nestjs/common';

import { OutboxInterceptor } from './outbox.interceptor';
import { OutboxRepository } from './outbox.repository';
import { OutboxService } from './outbox.service';

@Module({
  providers: [OutboxInterceptor, OutboxRepository, OutboxService],
  exports: [OutboxInterceptor],
})
export class OutboxModule {}
