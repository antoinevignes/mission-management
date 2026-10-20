---
name: units-and-time
description: Rules for timestamps, time zones, durations and physical units (UTC, Instant, Clock, explicit unit suffixes). Use when adding or reviewing any date, time, duration, weight or distance field.
---

# Units and time

## Time
- Store and exchange timestamps in UTC as `Instant` (or `OffsetDateTime` at API boundaries). Never `LocalDateTime` for a moment in time.
- `LocalDate` only for pure calendar dates (e.g. `licenseExpiration`).
- Set `spring.jpa.properties.hibernate.jdbc.time_zone=UTC` so Hibernate writes UTC regardless of the JVM zone.
- Use `Duration` for durations, not an `Integer` of unknown unit.
- Never call `Instant.now()` / `LocalDate.now()` in business code: inject a `Clock` bean, and use `Clock.fixed(...)` in tests.
- JSON: ISO-8601 with `Z` (e.g. `2026-10-20T14:30:00Z`).

## Units
- Every numeric physical quantity carries its unit in the name: `maxPayloadKg`, `maxFlightTimeMinutes`, `distanceNm`, `altitudeFt`.
- Aviation conventions: distance in nautical miles (`Nm`), altitude in feet (`Ft`), speed in knots (`Kt`), mass in kg.
- Column names follow: `max_payload_kg`, `max_flight_time_minutes`.
- Weights use `BigDecimal` (no floating point rounding).

## Known debt
`createdAt`/`updatedAt` are `LocalDateTime` and `Aircraft.maxFlightTime` / `maxPayload` have no unit. Fix with the Flyway step (step 1), when the schema is rewritten anyway.
