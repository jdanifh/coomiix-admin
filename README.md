# Coomiix Admin

A Spring Boot service for managing admin resources (players, etc.). It follows a clean layered architecture with domain-driven design concepts, OpenAPI-first contracts, and asynchronous integration via RabbitMQ.

## Overview
- **Language/Runtime**: Java 21
- **Framework**: Spring Boot 3.5.3
- **Data Store**: MongoDB
- **Messaging**: RabbitMQ
- **AuthN/Z**: Keycloak (OIDC/JWT Resource Server)
- **API Contract**: OpenAPI 3 (codegen at build)
- **Reverse Proxy**: Traefik (local compose)
- **Build**: Gradle
- **Testing**: JUnit 5, Mockito, Testcontainers

## Architecture
The codebase is organized by vertical feature (`player`) and horizontal layers, embracing domain-driven design and ports/adapters.

- **Application layer** (`src/main/java/.../player/application`)
  - Use-cases/services coordinate operations and apply application-level rules (e.g., `CreatePlayerService`).
  - Input is expressed as commands/DTOs (e.g., `CreatePlayerCommand`).
- **Domain layer** (`src/main/java/.../player/domain`)
  - Pure domain model (`Player`, value objects like `Email`).
  - Domain events (`PlayerCreatedEvent`) describe meaningful business occurrences.
  - `PlayerRepository` is a port that the domain/application depend on.
- **Infrastructure layer** (`src/main/java/.../player/infrastructure`)
  - Adapters implementing ports, e.g., MongoDB repository (`PlayerMongodbRepository`).
  - HTTP controllers (`PlayerRestController`) mapping OpenAPI to application services.
  - Mappers between domain and persistence/transport (`PlayerDocumentMapper`, `PlayerResponseMapper`).
- **Shared** (`src/main/java/.../shared`)
  - Cross-cutting concerns: domain `EventPublisher`, messaging (RabbitMQ), persistence config (MongoDB), controller exception handling, and base exceptions.

### Events and Messaging
- Domain events are published via an `EventPublisher` implementation that uses RabbitMQ (see `shared/infrastructure/messaging`).
- This enables loose coupling between write operations and downstream consumers.

### Security
- Resource server validates JWTs from Keycloak.
- Issuer and JWK set URIs are configured via `application.properties`.

## Project Structure
```
coomiix-admin/
  ├─ src/
  │  ├─ main/
  │  │  ├─ java/com/coomiix/admin/
  │  │  │  ├─ AdminApplication.java
  │  │  │  ├─ player/
  │  │  │  │  ├─ application/  # use-cases (create, update, delete, search)
  │  │  │  │  ├─ domain/       # entities, value objects, events, repository port
  │  │  │  │  └─ infrastructure/ # controllers, repository adapters, mappers
  │  │  │  └─ shared/          # common domain+infra (events, exceptions, config)
  │  │  └─ resources/
  │  │     ├─ application.properties
  │  │     └─ openapi.yaml     # API contract (OpenAPI 3)
  │  └─ test/
  │     └─ java/...            # unit/integration tests (incl. Testcontainers)
  ├─ Dockerfile
  ├─ compose.yaml
  ├─ build.gradle
  └─ settings.gradle
```

## API
- Contract: `src/main/resources/openapi.yaml` (title: "Admin API", version: 1.0.0)
- Notable paths: `/players` (POST create, GET search), `/players/{id}` (GET)
- OpenAPI UI available when running locally via Springdoc (check `/swagger-ui.html` or `/v3/api-docs` if enabled through Traefik path rules).


## Running Locally
### Prerequisites
- Java 21, Gradle (or use the Gradle wrapper)
- Docker + Docker Compose (for full stack)

### Option A: Run with Gradle (app only)
```bash
./gradlew clean bootRun
```
Defaults:
- App listens on port 8080.
- Requires running dependencies (MongoDB, RabbitMQ, Keycloak). You can launch them via Docker Compose while running the app locally:
```bash
docker compose up -d mongodb rabbitmq keycloak postgres traefik
```

### Option B: Full stack with Docker Compose
```bash
docker compose up --build
```
Services/ports:
- App behind Traefik on `http://localhost` with path prefix `/api` (per labels). The container listens on 8080 and is mapped to host `127.0.0.1:8081`.
- Traefik: `http://localhost` (web), dashboard at `127.0.0.1:8083`.
- Keycloak: `http://localhost:8080` (dev mode).
- MongoDB: `127.0.0.1:27017`.
- RabbitMQ: `5672`.
- Postgres (for Keycloak): `127.0.0.1:5432`.

Note: The Dockerfile exposes 8081 but the Spring app serves on 8080; Compose maps `8081:8080` for host access.

## Build, Test, and Codegen
- Build: `./gradlew build`
- Run tests: `./gradlew test`
- OpenAPI code generation runs automatically before compilation (`openApiGenerate` task). Generated sources are placed in `build/generated/src/main/java` and added to the main source set.

## Development Notes
- Use MapStruct for DTO/entity/document mapping. Annotations are processed at build-time.
- Domain events should be raised from the domain model or application layer and published via the shared `EventPublisher`.
- Exception handling is centralized in `shared/infrastructure/controller/ControllerExceptionHandler`.

## Troubleshooting
- If JWT validation fails, ensure Keycloak realm `coomiix` is running at `http://localhost:8080` and the client `coomiix-admin` exists with the configured secret.
- Verify `.env` values match those expected by `compose.yaml` and `application.properties`.
- For MongoDB auth, ensure `authSource=admin` if using root credentials.
