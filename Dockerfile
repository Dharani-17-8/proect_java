
# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . /app
RUN mvn -DskipTests clean package

# --- Runtime stage ---
FROM eclipse-temurin:17-jre
WORKDIR /opt/app

# Copy built jar
COPY --from=builder /app/target/inventory-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Optional: env vars for DB (can override at runtime)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/inventorydb     SPRING_DATASOURCE_USERNAME=postgres     SPRING_DATASOURCE_PASSWORD=root     SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]
