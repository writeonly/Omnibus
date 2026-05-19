import type { Request } from 'express';

export interface HttpOutboxEvent {
  eventId: string;
  eventType: string;
  aggregateType: string;
  aggregateId: string;
  occurredAt: string;
  method: string;
  path: string;
  statusCode: number;
  durationMs: number;
  request: {
    body: unknown;
    query: unknown;
    params: unknown;
    headers: Record<string, string | string[] | undefined>;
  };
  response?: unknown;
  error?: {
    name: string;
    message: string;
  };
}

export type HttpRequest = Request & {
  body?: unknown;
  route?: {
    path?: string;
  };
};
