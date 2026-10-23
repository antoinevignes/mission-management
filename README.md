# Mission Management

[![CI](https://github.com/antoinevignes/mission-management/actions/workflows/ci.yaml/badge.svg)](https://github.com/antoinevignes/mission-management/actions/workflows/ci.yaml)

API REST de gestion de missions aériennes : flotte d'aéronefs, opérateurs (pilotes) et, à terme, planification et suivi des missions.

## Stack

- Java 21, Spring Boot 4, Maven
- Spring Data JPA / Hibernate, PostgreSQL 17
- Flyway (migrations du schéma), Bean Validation
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, AssertJ, MockMvc
- GitHub Actions (CI)

## Démarrage

Prérequis : JDK 21 et Docker.

```bash
cp .env.example .env    # renseigner les identifiants PostgreSQL
./mvnw spring-boot:run  # démarre aussi PostgreSQL via compose.yaml
```

Documentation de l'API : http://localhost:8080/swagger-ui.html

## Endpoints

| Ressource  | Base               | Opérations                         |
|------------|--------------------|------------------------------------|
| Aéronefs   | `/api/v1/aircrafts` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| Opérateurs | `/api/v1/operators` | `POST`, `GET`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |

## Tests

```bash
./mvnw verify
```

Le test de chargement du contexte (`MissionManagementApplicationTests`) démarre PostgreSQL via Docker Compose ; les autres tests sont des tests unitaires et de slice.

## Conventions

- Horodatages en UTC (`Instant`), `Clock` injectée.
- Unités explicites dans les noms (`maxPayloadKg`, `maxFlightTimeMinutes`).
- Schéma géré par Flyway, Hibernate en `ddl-auto=validate`.
