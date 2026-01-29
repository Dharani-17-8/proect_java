
# Inventory API (Spring Boot + PostgreSQL)

Implements a simple product inventory REST API with CRUD operations, validation, optional filters, and Swagger UI.

## Requirements
- Java 17+
- Maven 3.9+
- PostgreSQL running locally (or via Docker Compose)

## Dev Run (PostgreSQL)
1. Update DB creds in `src/main/resources/application.properties`.
2. Start Postgres (local or via Docker Compose):
   ```bash
   docker compose up -d postgres
   ```
3. Run the app:
   ```bash
   mvn spring-boot:run
   ```
4. Swagger UI → `http://localhost:8080/swagger-ui.html`

## Build JAR
```bash
mvn clean package
```

## Docker (multi-stage)
Build and run container (uses the JAR produced by Maven):
```bash
# Build image
docker build -t inventory-api:latest .

# Run (expects Postgres at localhost:5432 unless overridden)
docker run --rm -p 8080:8080   -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/inventorydb   -e SPRING_DATASOURCE_USERNAME=postgres   -e SPRING_DATASOURCE_PASSWORD=root   inventory-api:latest
```

## Docker Compose (optional)
```bash
docker compose up -d
# App at http://localhost:8080, Swagger at /swagger-ui.html
```

## Endpoints
- `POST /api/products` — Create
- `GET /api/products` — List (filters: `category`, `maxPrice`)
- `GET /api/products/{id}` — Get by ID
- `PUT /api/products/{id}` — Update (full)
- `DELETE /api/products/{id}` — Delete

## Validation & Rules
- name: 3–100, not blank
- description: ≤ 300
- price: > 0
- quantity: ≥ 0 (negative updates rejected)

