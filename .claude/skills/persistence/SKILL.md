---
name: persistence
description: JPA/Hibernate and PostgreSQL conventions for mission-management (entities, repositories, uniqueness checks, schema management, transactions). Use when adding or changing entities, repositories, queries or the schema.
---

# Persistence

## Entities (model: `entity/aircraft/Aircraft.java`)
- Lombok `@Builder` / `@Getter` / `@Setter` (+ no-args/all-args constructors), no `@Data` on entities.
- `@Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;`
- Enums with `@Enumerated(EnumType.STRING)`.
- snake_case columns via `@Column(name = "...")`, `nullable`/`unique` declared explicitly.
- `@CreationTimestamp` / `@UpdateTimestamp` for `createdAt` / `updatedAt` (target type: `Instant`, see `units-and-time`).

## Repositories
- Spring Data JPA interfaces extending `JpaRepository<X, UUID>`.
- Uniqueness checked in the service before saving: `existsBy<Field>` (create), `existsBy<Field>AndIdNot` (update). Keep the DB `unique` constraint as the safety net.

## Schema
- Flyway owns the schema: `src/main/resources/db/migration/V<n>__<description>.sql`. Never edit an applied migration; add a new `V<n+1>`.
- `ddl-auto=validate`: Hibernate only checks entities against the tables. Column types must match exactly (`varchar(255)` for `String`, `numeric(p,s)` matching `@Column(precision, scale)`, `timestamptz` for `Instant`).
- Enum columns carry a `CHECK (col IN (...))` constraint: adding an enum value requires a migration.
- Postgres runs from `compose.yaml`; credentials from `.env` (never read it).

## Upcoming topics (steps 3, 7)
- Relations: prefer `@ManyToOne(fetch = LAZY)`, watch for N+1 (fetch join / `@EntityGraph`), `@Transactional` on service methods that write, `readOnly = true` on reads.
- Optimistic locking with `@Version`, audit with Hibernate Envers.

## Guidance
Learning project: explain the JPA concept and its pitfalls, let the user implement, then review.
