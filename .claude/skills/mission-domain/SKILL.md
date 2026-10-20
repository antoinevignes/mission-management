---
name: mission-domain
description: Aviation domain of mission-management (aircraft, operators, missions, statuses, assignment rules). Use when designing or reviewing entities, business rules or the Mission state machine.
---

# Mission domain

## Existing entities
- **Aircraft**: `registration` (unique, uppercase, e.g. `F-HABC`), `serialNumber` (unique, uppercase), `name`, `manufacturer`, `model`,
  `type` (`FIXED_WING`, `ROTARY_WING`, `UAV`), `status` (`AVAILABLE`, `IN_MISSION`, `MAINTENANCE`, `OUT_OF_SERVICE`),
  `maxFlightTime` and `maxPayload` (units not yet explicit: to rename, see `units-and-time`).
- **Operator** (pilot): `firstName`, `lastName`, `licenseNumber` (unique), `licenseExpiration` (`LocalDate`), `email`, `phone`,
  `status` (`AVAILABLE`, `ON_MISSION`, `OFF_DUTY`).
- New entities start with a server-set status (`AVAILABLE`), never taken from the request.

## Planned: Mission (roadmap steps 3-4)
- Links one aircraft and one operator, with a planned time slot (UTC `Instant` start/end), payload and duration.
- State machine: `PLANNED → BRIEFED → IN_FLIGHT → COMPLETED`, and `ABORTED` from any non-terminal state.
  Transitions are explicit and validated; `COMPLETED`/`ABORTED` are terminal.
- Assignment rules, each in its own testable validator:
  - operator licence not expired on the mission date;
  - no double booking: aircraft or operator slots must not overlap;
  - payload ≤ aircraft max payload, duration ≤ aircraft max flight time;
  - aircraft in `MAINTENANCE` or `OUT_OF_SERVICE` cannot be assigned.
- Concurrent assignments guarded by optimistic locking (`@Version`).

## Later (step 7)
Cumulated flight hours per aircraft (fed by completed missions), maintenance thresholds (e.g. every 100 h) switching the aircraft to `MAINTENANCE`, audit trail with Hibernate Envers.

## Guidance
This is a learning project: explain the domain rule and why, let the user implement, then review.
