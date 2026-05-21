select 'create database auth_db owner omnibus'
where not exists (select from pg_database where datname = 'auth_db')\gexec

select 'create database user_db owner omnibus'
where not exists (select from pg_database where datname = 'user_db')\gexec

select 'create database audit_db owner omnibus'
where not exists (select from pg_database where datname = 'audit_db')\gexec
