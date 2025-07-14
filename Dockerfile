# Multi-stage build
FROM openjdk:21 AS builder

# Copy source code
COPY . /app
WORKDIR /app

# Build the application
RUN ./mvnw clean package -DskipTests

# Final stage
FROM openjdk:21
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
LABEL authors="hung"

ENTRYPOINT ["java", "-jar", "/app.jar"]