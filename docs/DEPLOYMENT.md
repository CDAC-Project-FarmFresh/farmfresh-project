# Farm Fresh: deployment reference

This document describes the **tech stack**, **what you need to run locally**, **how to deploy locally**, and **practical cloud free-tier patterns**. It complements the root [README.md](../README.md).

---

## Tech stack

| Layer | Technology | Version (typical) | Notes |
|-------|------------|-------------------|--------|
| **API** | Java + Spring Boot | Java 17+, Spring Boot 3.4.x | REST API, context path `/FarmFresh` |
| **Security** | Spring Security + JWT | — | Access + refresh tokens; BCrypt for passwords |
| **ORM / DB** | Spring Data JPA + Hibernate | — | `ddl-auto: validate` (schema from Flyway) |
| **Migrations** | Flyway | — | SQL in `backend/src/main/resources/db/migration/` |
| **Database** | MySQL | 8.x | Required in production-like setups |
| **API docs** | springdoc-openapi | — | Swagger UI under `/FarmFresh/swagger-ui/index.html` |
| **Frontend** | React + Vite | React 18, Vite 6 | SPA; env-driven API base URL |
| **UI** | Bootstrap 5, react-bootstrap | — | Optional ThemeForest assets in `src/theme/` |
| **HTTP client** | Axios | — | Interceptors for `Authorization` + refresh |
| **Server state** | TanStack Query (React Query) | v5 | Product list and similar data fetching |
| **Containers** | Docker Compose | — | Optional local MySQL + API |

---

## Essentials for local run

Install or have available:

1. **JDK 17** (or 21; project targets 17 in Maven). Verify: `java -version`
2. **Apache Maven 3.8+**. Verify: `mvn -version`
3. **Node.js 20 LTS** (or 22; avoid odd versions like 21 if Vite warns about engine). Verify: `node -version`
4. **MySQL 8.0** running locally **or** Docker Desktop (if using Compose).
5. **Git** (to clone the repo).

Optional but useful:

- **curl** or **Postman** for API checks
- Browser devtools for CORS/JWT debugging

### Environment variables (local)

**Backend** (see [application.yml](../backend/src/main/resources/application.yml)):

| Variable | Purpose | Default (dev) |
|----------|---------|----------------|
| `SPRING_DATASOURCE_URL` | JDBC URL | `jdbc:mysql://localhost:3306/farmfresh?...` |
| `SPRING_DATASOURCE_USERNAME` | DB user | `root` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `manager` |
| `JWT_SECRET` | HMAC key for JWT (min ~32 bytes) | Dev default in `application.yml` (change for any shared env) |
| `JWT_ACCESS_MS` / `JWT_REFRESH_MS` | Token lifetimes | Optional overrides |
| `APP_CORS_ORIGINS` | Comma-separated browser origins | `http://localhost:3000,http://localhost:5173` |
| `SPRING_MAIL_*` | Outbound email | Optional; leave empty if not sending mail |

**Frontend** (file `.env` in `frontend/`, copy from [.env.example](../frontend/.env.example)):

| Variable | Purpose | Example |
|----------|---------|---------|
| `VITE_API_BASE_URL` | API origin **without** trailing slash | `http://localhost:8080/FarmFresh` |

### First-time database note

- On an **empty** MySQL database, Flyway runs `V1__initial_schema.sql` and the app seeds an **admin** user if no users exist (see README for email/password).
- If you already had an old schema (`user` table, etc.), either use a **fresh database name** or migrate manually; the current schema uses **`farm_user`** and related tables from Flyway.

### Troubleshooting: “Found non-empty schema … but no schema history table”

MySQL Workbench (or the server) can be running fine; this error is **Flyway**, not connectivity.

It happens when the `farmfresh` database **already had tables** (for example from an older app using Hibernate `ddl-auto=update`) but **`flyway_schema_history` was never created**.

**What the project does:** [application.yml](../backend/src/main/resources/application.yml) sets `spring.flyway.baseline-on-migrate: true` and `baseline-version: 0` so Flyway can create the history table on first run and then apply `V1__initial_schema.sql`.

**If startup still fails** (for example Hibernate `validate` complains because old table definitions conflict with the new model), use a clean database in MySQL Workbench:

```sql
DROP DATABASE IF EXISTS farmfresh;
CREATE DATABASE farmfresh CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Then start the API again so Flyway runs on an empty schema.

---

## Deploy locally (step by step)

### Option A: Native (good for development)

1. **Start MySQL** and ensure credentials match `SPRING_DATASOURCE_*` (or change env / defaults).

2. **Run the API** (from repo root):

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   - Health check in browser: `http://localhost:8080/FarmFresh/v3/api-docs` (JSON) or Swagger UI path above.
   - CORS must include your frontend origin; default includes `http://localhost:5173`.

3. **Run the SPA** (second terminal):

   ```bash
   cd frontend
   cp .env.example .env
   # Edit .env if your API is not on localhost:8080
   npm install
   npm run dev
   ```

4. Open **`http://localhost:5173`**, register a user or log in with the seeded admin (see README).

### Option B: Docker Compose (MySQL + API only)

From the **repository root** (where [docker-compose.yml](../docker-compose.yml) lives):

```bash
docker compose up --build
```

- API: `http://localhost:8080/FarmFresh`
- MySQL: `localhost:3306` (user/password per compose file; align `APP_CORS_ORIGINS` if you expose the UI elsewhere).

The Compose file in this repo **does not** build the Vite app as a container; run the frontend with **Option A step 3** against `http://localhost:8080/FarmFresh`, or add an `nginx`/static service later if you want a single compose stack.

### Production-like local build (smoke test)

```bash
# API
cd backend && mvn -DskipTests package && java -jar target/Spring_Boot_FarmFresh-*.jar

# Frontend static preview (after build)
cd frontend && npm run build && npm run preview
```

Point `VITE_API_BASE_URL` at your running API when using `preview`.

---

## Cloud deployment (free tier and low cost)

There is rarely one vendor that gives **always-free MySQL + always-free long-running JVM + always-free CDN** together. Typical patterns:

### Pattern 1: Free static frontend + free/low-cost API + managed or small MySQL

| Piece | Free-tier friendly options | Notes |
|-------|----------------------------|--------|
| **React build** | [Vercel](https://vercel.com), [Netlify](https://www.netlify.com), [Cloudflare Pages](https://pages.cloudflare.com) | Connect Git repo; build command `npm run build`, output directory `dist`. Set `VITE_API_BASE_URL` in the host’s env UI to your **public API URL** (no trailing slash). |
| **Spring Boot JAR** | [Render](https://render.com) Web Service, [Fly.io](https://fly.io), [Railway](https://railway.app) | Free tiers often **sleep** or have **credit limits**; set `SPRING_DATASOURCE_*`, `JWT_SECRET`, `APP_CORS_ORIGINS` (your Vercel/Netlify URL) in the service env. |
| **MySQL** | Small paid tier (PlanetScale-style MySQL-compatible, RDS, etc.) or **self-host** on a free VM | Pure free MySQL SaaS is limited; **Oracle Cloud “Always Free”** ARM VM can run Docker MySQL for learning/demo. |

**CORS:** Set `APP_CORS_ORIGINS` to your real frontend origin(s), e.g. `https://your-app.vercel.app`.

**JWT:** Set a strong `JWT_SECRET` (long random string) in production; never use the dev default.

### Pattern 2: Single free VM (heavier, flexible)

Use **Oracle Cloud Infrastructure Always Free** (ARM Ampere) or similar:

- Install Docker, run **MySQL** + **Spring Boot** (or build your API image) on the same VM.
- Put **Caddy** or **Nginx** in front for TLS termination and reverse proxy to the API and optionally serve the built SPA (`dist/`).

Good for demos and learning; you manage updates, backups, and security.

### Pattern 3: Docker image for API only

The API includes a [Dockerfile](../backend/Dockerfile). Build and push to a registry, then deploy the image on Fly.io, Render, Google Cloud Run (watch cold starts and JDBC timeouts), etc., with env vars pointing at MySQL.

### What to avoid on strict free tiers

- Assuming **free MySQL** + **always-on JVM** + **custom domain HTTPS** on one forever-free SKU; combine services or accept cold starts / credit caps.
- Committing real **DB passwords**, **JWT secrets**, or **SMTP passwords** to Git; use host secrets / env only.

---

## Checklist before any cloud deploy

- [ ] `JWT_SECRET` set to a unique, long secret  
- [ ] `SPRING_DATASOURCE_*` point to the cloud MySQL instance (SSL params as required)  
- [ ] `APP_CORS_ORIGINS` includes your production frontend URL(s)  
- [ ] Frontend `VITE_API_BASE_URL` matches the **public** API base (https, no trailing slash)  
- [ ] Flyway has run at least once on that database (empty DB is OK)  
- [ ] Optional: configure `SPRING_MAIL_*` if you need outbound email  

---

## Quick reference URLs (local defaults)

| Resource | URL |
|----------|-----|
| Frontend (Vite dev) | `http://localhost:5173` |
| API base | `http://localhost:8080/FarmFresh` |
| OpenAPI JSON | `http://localhost:8080/FarmFresh/v3/api-docs` |
| Swagger UI | `http://localhost:8080/FarmFresh/swagger-ui/index.html` |

---

## CI

The repository includes [`.github/workflows/ci.yml`](../.github/workflows/ci.yml) (`mvn verify` and `npm ci && npm run build`). Use the same commands locally before pushing when possible.
