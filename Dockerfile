# Multi-stage Dockerfile for Volunteer Duty Roster
# Stage 1: Build War
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production Runtime
FROM eclipse-temurin:17-jre-jammy
LABEL maintainer="Meet Jain (23102C0068) <meetjain6091>"
LABEL description="Volunteer Duty Roster Production Container"
WORKDIR /app

# Create unprivileged system user for security
RUN groupadd -r rostergroup && useradd -r -g rostergroup rosteruser

# Copy built war from builder
COPY --from=builder /app/target/volunteer-duty-roster.war /app/volunteer-duty-roster.war
RUN chown -R rosteruser:rostergroup /app

USER rosteruser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/login || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/volunteer-duty-roster.war"]
