# Medicare

Multi-tenant healthcare backend built with Java 21 and Spring Boot. The initial API supports patient registration, appointment booking, and medical-report metadata. Each hospital is a tenant.

## Included

- PostgreSQL persistence with Flyway schema migrations.
- OAuth2/JWT resource-server security.
- Tenant identity comes from the verified JWT `tenant_id` claim; the API does not accept a client-selected tenant header.
- Tenant-scoped patient, appointment, and report queries. Database foreign keys also prevent cross-hospital patient references.
- Hospital onboarding restricted to tokens with the `platform:admin` scope.
- A `virtualVisit` appointment flag is included as the integration point for a telehealth provider.
- Report endpoints store metadata and a storage object key only; file upload/download and video transport are intentionally not implemented.

## Run locally

Requirements: Java 21, Maven, PostgreSQL, and an OAuth2/OIDC identity provider that issues JWTs with a UUID `tenant_id` claim.

Create a PostgreSQL database named `medicare` and a matching database user, then set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` if the defaults do not match. Set `JWT_ISSUER_URI` to the identity provider's issuer URL. Start the API with `mvn spring-boot:run`; Flyway creates the tables on startup.

Use `POST /api/v1/platform/hospitals` with a platform-admin bearer token to onboard a hospital. The response includes its `tenantId`; provision users in the identity provider with that UUID as their `tenant_id` claim. Never let a user choose or change their tenant claim.

## API

- `POST /api/v1/patients` and `GET /api/v1/patients`
- `POST /api/v1/appointments` and `GET /api/v1/appointments`
- `POST /api/v1/patients/{patientId}/reports` and `GET /api/v1/patients/{patientId}/reports`
- `POST /api/v1/platform/hospitals` (requires the `platform:admin` scope)
- `GET /actuator/health`

All API routes except health require a valid bearer token. Example patient payload: `{"fullName":"Alex Doe","email":"alex@example.org","phone":"+1-555-0100"}`. Appointment timestamps must be ISO-8601 offset date-times.

## Production readiness

This is a backend foundation, not a certified clinical system. Before handling real patient information, add organization/user onboarding and role-based authorization, audit logging, consent and retention workflows, encrypted managed object storage with short-lived access links, rate limiting, backups, monitoring, and a telehealth provider integration. Complete the applicable privacy, security, and regulatory reviews for deployment jurisdiction. Do not place patient data in logs or source control.
