create table if not exists outbox_events (
  id uuid primary key,
  aggregate_type text not null,
  aggregate_id text not null,
  event_type text not null,
  payload jsonb not null,
  occurred_at timestamptz not null default now(),
  created_at timestamptz not null default now(),
  published_at timestamptz,
  publish_attempts integer not null default 0,
  last_publish_error text
);

create index if not exists idx_outbox_events_unpublished
  on outbox_events (created_at)
  where published_at is null;

create index if not exists idx_outbox_events_type_occurred
  on outbox_events (event_type, occurred_at);

create or replace function notify_outbox_events()
returns trigger as $$
begin
  perform pg_notify(
    'outbox_events',
    json_build_object(
      'id', new.id,
      'eventType', new.event_type,
      'aggregateType', new.aggregate_type,
      'aggregateId', new.aggregate_id
    )::text
  );

  return new;
end;
$$ language plpgsql;

drop trigger if exists trg_notify_outbox_events on outbox_events;

create trigger trg_notify_outbox_events
after insert on outbox_events
for each row
execute function notify_outbox_events();
