CREATE KEYSPACE IF NOT EXISTS omnibus
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
