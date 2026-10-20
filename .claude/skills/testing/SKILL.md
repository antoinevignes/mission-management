---
name: testing
description: How tests are written and run in mission-management (Mockito service tests, @WebMvcTest controller tests, Spring Boot 4 / Jackson 3 packages, surefire reports). Use when writing, reviewing or debugging tests.
---

# Testing

## Running
- `./mvnw -q test -Dtest=ClassName` (or `ClassName#method`). Avoid the full suite unless needed.
- On failure, read `target/surefire-reports/<fully.qualified.ClassName>.txt` (stack trace + assertion message) instead of rerunning verbosely.
- `MissionManagementApplicationTests` needs Postgres running; Testcontainers will remove that need (roadmap step 5).

## Service tests (model: `src/test/java/.../service/AircraftServiceTest.java`)
- `@ExtendWith(MockitoExtension.class)`, `@Mock` repository + mapper, `@InjectMocks` service.
- Private builder helpers (`createAircraft(UUID)`, `createAircraftRequest()`) for fixtures.
- AssertJ: `assertThat(...)`, `assertThatThrownBy(...).isInstanceOf(...)`.
- Cover the happy path and each exception (`NotFound`, `AlreadyExists` on create and update), and `verify(repository, never()).save(any())` when rejected.

## Controller tests (model: `src/test/java/.../controller/AircraftControllerTest.java`)
- `@WebMvcTest(XController.class)`, `@Autowired MockMvc`, `@MockitoBean` service, `@Autowired ObjectMapper`.
- Spring Boot 4 packages: `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`, `org.springframework.test.context.bean.override.mockito.MockitoBean`, Jackson 3 `tools.jackson.databind.ObjectMapper`.
- Assert status codes (201/200/204/400/404/409) and JSON with `jsonPath`; on 400 assert `verifyNoInteractions(service)`.

## Guidance
Learning project: point the user at the model test and the cases to cover, let them write it, then review.
