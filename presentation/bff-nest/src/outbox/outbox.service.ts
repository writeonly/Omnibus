import { Injectable, Logger } from '@nestjs/common';
import { randomUUID } from 'crypto';

import { OutboxRepository } from './outbox.repository';
import type { HttpOutboxEvent, HttpRequest } from './outbox.types';

@Injectable()
export class OutboxService {
  private readonly logger = new Logger(OutboxService.name);

  constructor(private readonly repository: OutboxRepository) {}

  async recordHttpResult(input: {
    request: HttpRequest;
    statusCode: number;
    durationMs: number;
    response?: unknown;
    error?: Error;
  }): Promise<void> {
    const event = this.buildHttpEvent(input);

    try {
      await this.repository.save(event);
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      this.logger.error(`Unable to persist outbox event ${event.eventId}: ${message}`);
    }
  }

  private buildHttpEvent(input: {
    request: HttpRequest;
    statusCode: number;
    durationMs: number;
    response?: unknown;
    error?: Error;
  }): HttpOutboxEvent {
    const eventId = randomUUID();
    const method = input.request.method;
    const path = input.request.originalUrl ?? input.request.url;

    return {
      eventId,
      eventType: input.error ? 'bff.http.request.failed' : 'bff.http.request.completed',
      aggregateType: 'bff-http-request',
      aggregateId: `${method} ${path}`,
      occurredAt: new Date().toISOString(),
      method,
      path,
      statusCode: input.statusCode,
      durationMs: input.durationMs,
      request: {
        body: sanitize(input.request.body),
        query: sanitize(input.request.query),
        params: sanitize(input.request.params),
        headers: sanitizeHeaders(input.request.headers),
      },
      response: input.response === undefined ? undefined : sanitize(input.response),
      error: input.error
        ? {
            name: input.error.name,
            message: input.error.message,
          }
        : undefined,
    };
  }
}

function sanitize(value: unknown): unknown {
  if (value === undefined || value === null) {
    return value;
  }

  return JSON.parse(
    JSON.stringify(value, (key, nestedValue) => (isSensitiveKey(key) ? '[REDACTED]' : nestedValue))
  );
}

function sanitizeHeaders(headers: HttpRequest['headers']) {
  return Object.fromEntries(
    Object.entries(headers).map(([key, value]) => [key, isSensitiveKey(key) ? '[REDACTED]' : value])
  );
}

function isSensitiveKey(key: string): boolean {
  return ['authorization', 'cookie', 'set-cookie', 'password', 'token'].some((part) =>
    key.toLowerCase().includes(part)
  );
}
