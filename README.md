# Farm Fresh

Organic marketplace monorepo: Spring Boot API (`backend/`), React SPA (`frontend/`), MySQL schema via Flyway.

## Layout

| Path | Purpose |
|------|---------|
| `backend/` | Spring Boot Maven module (`pom.xml`, `Dockerfile`, `run.sh`, Flyway SQL) |
| `frontend/` | Vite + React app |
| `docs/` | [DEPLOYMENT.md](docs/DEPLOYMENT.md), design diagrams under `docs/diagrams/` |

## Quick start (local)

1. **MySQL 8** — create database `farmfresh` (or let JDBC `createDatabaseIfNotExist` create it). Default dev URL in `application.yml`: `jdbc:mysql://localhost:3306/farmfresh` user `root` / `manager`. Override with `SPRING_DATASOURCE_*` env vars. If you see a Flyway error about a **non-empty schema** and **no schema history table**, see [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) (troubleshooting) or drop/recreate the `farmfresh` database for a clean migrate.

2. **API** — from `backend/`:

   ```bash
   cp .env.example .env   # optional: set JWT_SECRET, DB password, mail, etc.
   ./run.sh run           # or: mvn spring-boot:run
   ```

   `run.sh` loads `.env` from this same folder (see [`.env.example`](backend/.env.example)). Run `./run.sh help` for Maven phases (`package`, `jar`, …).

   - Context path: `http://localhost:8080/FarmFresh`
   - Swagger UI: `http://localhost:8080/FarmFresh/swagger-ui/index.html`
   - On an **empty** database, Flyway applies [`V1__initial_schema.sql`](backend/src/main/resources/db/migration/V1__initial_schema.sql) and a seed **admin** is created if no users exist:
     - Email: `admin@farmfresh.local`
     - Password: `Admin#12345`

3. **Web** — from `frontend/`:

   ```bash
   cp .env.example .env
   npm install
   npm run dev
   ```

   Vite dev server: `http://localhost:5173`. Set `VITE_API_BASE_URL` in `.env` to match the API (no trailing slash).

## Docker

From repo root:

```bash
docker compose up --build
```

Starts MySQL and the API. Use the same seed admin on first empty DB.

## CI

GitHub Actions workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) runs `mvn verify` on `backend/` and `npm ci && npm run build` on `frontend/` (branches: `main`, `master`, `develop`).

## ThemeForest template

Licensed HTML template assets can replace or extend [`frontend/src/theme/farm-theme.css`](frontend/src/theme/farm-theme.css) and layout components under `frontend/src/Component`.
