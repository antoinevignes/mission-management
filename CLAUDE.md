# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4 (Java 21, Maven) REST API for managing aviation missions: aircraft and operators (pilots), backed by PostgreSQL. Learning project: the user writes the code, Claude explains, assigns precise objectives and reviews like a PR colleague; Claude only writes boilerplate/config. One branch + PR per roadmap step. Commit messages are mostly in French.

## Commands

Use quiet mode to keep output short:

```bash
./mvnw -q compile                                  # compile only
./mvnw -q test -Dtest=AircraftServiceTest          # one test class (or Class#method)
./mvnw -q test                                     # all tests (needs Postgres, see below)
./mvnw spring-boot:run                             # run the app (starts Postgres via compose.yaml)
```

- On test failure, read `target/surefire-reports/<TestClass>.txt` instead of rerunning verbosely.
- `MissionManagementApplicationTests` (`@SpringBootTest`) loads the full context and needs Postgres running (`docker compose up -d`; it sets `spring.docker.compose.skip.in-tests=false` to get the connection from compose); the other tests are pure unit/slice tests.
- DB credentials come from `.env` (template: `.env.example`). Never read `.env`.
- Schema is managed by Flyway (`src/main/resources/db/migration/V<n>__<description>.sql`); Hibernate runs with `ddl-auto=validate`. Swagger UI: `/swagger-ui.html`. Logs: console + `logs/mission-management.log`.

## Non-negotiable rules

- **Time is UTC**: timestamps are `Instant` (or `OffsetDateTime`), never `LocalDateTime`. `LocalDate` only for calendar dates (e.g. licence expiration). Inject a `Clock` instead of calling `now()` directly.
- **Units are explicit**: in names (`maxFlightTimeMinutes`, `maxPayloadKg`) or via `Duration`. Never a bare number whose unit must be guessed.
- **No `git push`**: the user pushes and opens PRs.
- Use the built-in Explore agent for broad searches; read only the files needed.

## Architecture

Layered layout under `com.antoinevignes.missionmanagement`, per-domain subpackages (`aircraft`, `operator`) inside `entity`, `dto` and `exception`:
`controller` → `service` → `repository` (Spring Data JPA), with `mapper` converting between entities and DTOs. Mirror the Aircraft implementation (most complete, has tests).

- **Controllers**: `/api/v1/<plural>`, UUID path variables, `ResponseEntity` (201 create, 204 delete), `@Valid @RequestBody`.
- **DTOs**: `record`s. One `Create<X>Request` for POST and PUT (full replacement). Validation messages as `private static final String ERROR_*` constants; compact constructor normalizes input (strip, uppercase identifiers).
- **Mappers**: hand-written `@Component` (no MapStruct): `toEntity`, `update(entity, request)`, `toResponse`. Server-controlled fields (initial `status`) set in `toEntity`.
- **Services**: uniqueness via `existsBy<Field>` (create) / `existsBy<Field>AndIdNot` (update), domain exceptions (`<X>NotFoundException`, `<X>AlreadyExistsException`), `@Slf4j` logs on mutations.
- **Errors**: each domain exception has a handler in `exception/GlobalExceptionHandler` returning `ErrorResponse` (404 / 409 / 400 with field→message map).

Details live in skills: `mission-domain`, `units-and-time`, `testing`, `persistence`.

## Code style

2-space indentation, Google Java Format (auto-applied by a hook after each edit). Service methods are preceded by an uppercase section comment (e.g. `//  CREATE AIRCRAFT`).
