import { Injectable, Logger, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import { Pool } from 'pg';

import { readOutboxConfig } from './outbox.config';
import type { HttpOutboxEvent } from './outbox.types';

@Injectable()
export class OutboxRepository implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(OutboxRepository.name);
  private readonly config = readOutboxConfig();
  private readonly pool = new Pool({
    connectionString: this.config.databaseUrl,
  });

  async onModuleInit(): Promise<void> {
    if (!this.config.enabled) {
      this.logger.warn('BFF outbox is disabled');
      return;
    }

    await this.pool.query('select 1');
    this.logger.log('BFF outbox connected to Postgres');
  }

  async onModuleDestroy(): Promise<void> {
    await this.pool.end();
  }

  async save(event: HttpOutboxEvent): Promise<void> {
    if (!this.config.enabled) {
      return;
    }

    await this.pool.query(
      `
        insert into outbox_events (
          id,
          aggregate_type,
          aggregate_id,
          event_type,
          payload,
          occurred_at
        )
        values ($1, $2, $3, $4, $5::jsonb, $6::timestamptz)
      `,
      [
        event.eventId,
        event.aggregateType,
        event.aggregateId,
        event.eventType,
        JSON.stringify(event),
        event.occurredAt,
      ]
    );
  }
}
