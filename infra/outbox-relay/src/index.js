import { Kafka } from 'kafkajs';
import pg from 'pg';

const databaseUrl = process.env.OUTBOX_DATABASE_URL ?? 'postgres://omnibus:omnibus@postgres:5432/omnibus';
const kafkaBootstrapServers = (process.env.KAFKA_BOOTSTRAP_SERVERS ?? 'kafka:9092').split(',');
const topic = process.env.OUTBOX_KAFKA_TOPIC ?? 'bff.outbox.events';
const pollIntervalMs = Number(process.env.OUTBOX_POLL_INTERVAL_MS ?? 5000);
const batchSize = Number(process.env.OUTBOX_BATCH_SIZE ?? 50);

const pool = new pg.Pool({ connectionString: databaseUrl });
const listener = new pg.Client({ connectionString: databaseUrl });
const kafka = new Kafka({
  clientId: 'omnibus-outbox-relay',
  brokers: kafkaBootstrapServers
});
const producer = kafka.producer();

let draining = false;

async function drainOutbox() {
  if (draining) {
    return;
  }

  draining = true;
  const client = await pool.connect();

  try {
    await client.query('begin');

    const result = await client.query(
      `
        select id, event_type, aggregate_id, payload
        from outbox_events
        where published_at is null
        order by created_at
        limit $1
        for update skip locked
      `,
      [batchSize],
    );

    for (const row of result.rows) {
      try {
        await producer.send({
          topic,
          messages: [
            {
              key: row.aggregate_id,
              headers: {
                event_id: row.id,
                event_type: row.event_type
              },
              value: JSON.stringify(row.payload)
            }
          ]
        });

        await client.query(
          `
            update outbox_events
            set published_at = now(),
                publish_attempts = publish_attempts + 1,
                last_publish_error = null
            where id = $1
          `,
          [row.id],
        );
      } catch (error) {
        const message = error instanceof Error ? error.message : String(error);
        await client.query(
          `
            update outbox_events
            set publish_attempts = publish_attempts + 1,
                last_publish_error = $2
            where id = $1
          `,
          [row.id, message],
        );
      }
    }

    await client.query('commit');
  } catch (error) {
    await client.query('rollback');
    console.error('Outbox drain failed', error);
  } finally {
    client.release();
    draining = false;
  }
}

async function start() {
  await producer.connect();
  await listener.connect();
  await listener.query('listen outbox_events');
  listener.on('notification', () => {
    void drainOutbox();
  });

  setInterval(() => {
    void drainOutbox();
  }, pollIntervalMs);

  await drainOutbox();
  console.log(`Outbox relay publishing to Kafka topic ${topic}`);
}

process.on('SIGTERM', async () => {
  await listener.end();
  await producer.disconnect();
  await pool.end();
  process.exit(0);
});

void start();
