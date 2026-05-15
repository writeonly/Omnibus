## README

Written with Spring Authorization Server.
It is not alternative for Keycloak.
It is wapper for Keycloak.
Use Posgres for Data.
Use Redis for Cache.

## TODO

- Register flow: Client -> BFF -> Gateway -> gRPC -> User Service -> Postgre -> Outbox -> Kafka -> Auth Server -> Spring Cloud Function -> Keycloak
- Loging flow: Client -> BFF -> Auth Server -> Keycloak -> JWT
- Logout flow: Client -> BFF -> Auth Server -> Keycloak logout + blacklist
