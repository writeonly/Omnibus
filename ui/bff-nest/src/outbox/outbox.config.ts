export interface OutboxConfig {
  databaseUrl: string;
  enabled: boolean;
}

export function readOutboxConfig(): OutboxConfig {
  return {
    databaseUrl:
      process.env.BFF_OUTBOX_DATABASE_URL ??
      process.env.DATABASE_URL ??
      'postgres://omnibus:omnibus@localhost:5432/omnibus',
    enabled: (process.env.BFF_OUTBOX_ENABLED ?? 'true') === 'true',
  };
}
