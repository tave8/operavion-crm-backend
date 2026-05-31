# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`zerochiamate-backend` is the Spring Boot 4.0.5 / Java 21 REST API for a multi-tenant SaaS that manages cleaning/service companies: their clients, client addresses, checklists, tasks, shifts, operators, contract expectations, and billing. Built with Maven, PostgreSQL, Flyway, and a long list of external integrations (Stripe, Resend, Cloudinary, Cloudflare R2, Anthropic, Geoapify). Deployed on Railway.

## Commands

Use the Maven wrapper (`mvnw` / `mvnw.cmd`). The project requires a set of environment variables to start (see "Environment & configuration").

```bash
./mvnw spring-boot:run          # run the app
./mvnw clean package            # build the fat jar (runs tests)
./mvnw clean package -DskipTests
./mvnw test                     # all tests
./mvnw test -Dtest=CsvGenerationTest          # one test class
./mvnw test -Dtest=CsvGenerationTest#methodName   # one test method
```

Local runs activate the `local` Spring profile (`application-local.properties`) and set `whereami=LOCAL`. The app refuses to start unless `whereami` is exactly `LOCAL`, `PREVIEW`, or `PRODUCTION` (validated in `AppEnvironment`).

Stripe webhooks locally need the Stripe CLI forwarder, otherwise webhook events never arrive:
```bash
stripe listen --forward-to localhost:3001/webhooks/stripe
```

Railway deployment (`railway` CLI): see `README.md` for `railway link` / `railway logs` usage. `HELP.md` and `README.md` together document every HTTP endpoint and the entity model.

## Package architecture

Everything lives under `giuseppetavella.zero_chiamate`. The package layout encodes the layering — keep new code in the matching layer:

- **`api/controllers/`** — thin REST controllers. They extract `currentUser` via `@AuthenticationPrincipal User`, validate payloads, and delegate to a service. `api/controllers/export/` produces CSV/PDF downloads; `api/webhooks/` handles Stripe.
- **`domain/entities/<entity>/`** — one folder per entity, each containing the JPA `Entity`, a Spring Data `*Repository`, a `*Service` (business logic), and `dto/sent/` + `dto/to_send/` DTOs. This is where most CRUD logic lives.
- **`domain/business/`** — cross-entity business processes: `auth`, `billing`, `jobs` (cron job executors), `reports` (PDF/CSV report builders), `cv_extraction`.
- **`infrastructure/`** — domain-agnostic technical capabilities: `ai`, `csv`, `email`, `geocoding`, `notification`, `pdf`, `qr_code`, `storage`, `template` (Thymeleaf), `workers` (async), and `jobs/job_library` (the reusable cron-job framework).
- **`integrations/`** — thin clients wrapping a single external API each (`anthropic`, `cloudflare_r2`, `cloudinary`, `geoapify`, `resend`, `stripe`), each with its own `exceptions/` subpackage.
- **`config/`** — `@Configuration` beans (security, CORS, Flyway, async, API clients, `AppEnvironment`).
- **`security/`** — `TokenFilter` (JWT request filter) and `TokenTools` (JWT encode/decode).
- **`helpers/`** — stateless static utility classes (`StringHelper`, `PayloadValidationHelper`, `AuthorizationHelper`, `TimeHelper`, …).
- **`exceptions/`** — domain exception types plus `ErrorsHandler` (the global `@RestControllerAdvice`).
- **`runners/`** — `ApplicationRunner` demos/experiments, not production code paths.

## Conventions that span multiple files

**Multi-tenancy by Company.** Almost all data is scoped to a `Company`. The standard flow: controller receives `@AuthenticationPrincipal User currentUser`, calls `currentUser.getCompany()`, and passes that `Company` into the service, which scopes every query by it. When adding a query, scope it by company — there is no automatic tenant filter.

**Authentication & authorization.** Stateless JWT. `SecurityConfig` deliberately `permitAll()` at the filter-chain level — authorization is **not** done there. Instead, every protected endpoint carries a method-level `@PreAuthorize("hasAnyAuthority('ADMIN')")` (roles from `UserRole`), enabled by `@EnableMethodSecurity`. A new endpoint with no `@PreAuthorize` is effectively public.

**DTO naming.** Incoming request bodies are `*SentDTO` (sent *by* the client) in `dto/sent/`; outgoing responses are `*ToSendDTO` (to *be sent to* the client) in `dto/to_send/`. DTOs are Java records; map entity→DTO in the constructor (`new TaskToSendDTO(task)`).

**Request validation.** Controllers take `@Validated SomeSentDTO body, BindingResult validation`, then immediately call `PayloadValidationHelper.requireNoErrors(validation)` before using the body. Query-param whitelisting uses `StringHelper.requireInValues(...)`.

**Error handling.** Throw a domain exception (`NotFoundException`, `UnauthorizedException`, `InvalidDataException`, `ShiftException`, …) from services; the global `ErrorsHandler` maps each to an HTTP status and a uniform `ErrorsToSendDTO`. For server-side faults (5xx, AI/Stripe/DB integrity errors) it also calls `ProblemsEmailService.alertDevIfNonLocal(...)` to email the developer in non-local environments. To surface a new error type with a specific status, add an `@ExceptionHandler` there rather than building a `ResponseEntity` in the controller.

**Environment switching.** Inject `AppEnvironment` and use `isLocal()/isPreview()/isProduction()`, `getFrontendUrl()`, `buildFrontendUrl(path)`, `buildServerUrl(path)` instead of reading `whereami`/URL properties directly. URLs are validated at startup.

## Database & migrations

Flyway owns the schema; Hibernate is `ddl-auto=none` and must never alter tables. Migrations live in `src/main/resources/db/migration/` named `V<n>__description.sql` — add a new versioned file, never edit an applied one.

Important gotcha: **Spring Boot 4.0.5 does not auto-configure Flyway**, so the `spring.flyway.*` properties in `application.properties` are ignored. Flyway is wired up manually as a bean in `config/FlywayConfig`. Change Flyway behavior there, not via properties.

Auditing uses **Hibernate Envers** — `@Audited` entities get `*_aud` shadow tables (created by the `V5`–`V7` migrations) and share a revision sequence.

## Background job framework

A custom cron-job library in `infrastructure/jobs/job_library` with business jobs in `domain/business/jobs/`. Four moving parts:

1. **`JobScheduler`** (`domain/business/jobs`) — defines *when*. Each `@Scheduled(cron = CronSchedule.X, zone = "Europe/Rome")` method makes exactly one `jobManager.executeJob(JobName.X)` call. Cron constants live in `CronSchedule`.
2. **`JobManager`** — defines *how*. Loops over "next items" and re-processes "incomplete executions", persisting a `JobExecution` row per item and emailing the dev on failure. Centralized try/catch; domain errors mark an execution `FAILED`, system errors abort the job.
3. **`JobExecutors`** — maps `JobName` → the concrete `JobExecutor` bean.
4. **`JobExecutor<T>`** (abstract) — each job subclasses it and implements `getNextItem()`, `processItem(...)`, `getItemByIdOnIncompleteExecution(...)`. `doProcessItem` is `@Transactional` (business writes + execution metadata commit/rollback together); `maxRetries` is 1–10.

To add a job: add a `JobName` enum value, create a `*_JobExecutor` under `domain/business/jobs/<job>/`, register the mapping in `JobExecutors.getJobExecutor(...)`, and add a `@Scheduled` method in `JobScheduler`. The framework assumes a **single replica** — multiple replicas would double-fire every cron.

## Time

JSON serialization is fixed to UTC (`spring.jackson.time-zone=UTC`); cron jobs run in `Europe/Rome`. `TODO.md` notes an outstanding timezone-consistency issue when persisting timestamps — be deliberate about timezones when touching date/time persistence.

## Notable stack details

Kotlin is configured in the build (`kotlin-maven-plugin`) alongside Java even though current sources are Java. Text extraction from uploaded documents (CVs, contracts) uses Apache Tika; PDFs are generated with flying-saucer from Thymeleaf templates; QR codes use zxing. OpenAPI/Swagger UI is available via springdoc.