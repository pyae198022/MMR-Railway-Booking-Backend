# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Copy frontend build (must be built separately)
# Run: cd mmr-railway-booking && npm run build
# Then copy dist to src/main/resources/static before building backend
COPY target/MMR_Railway_Booking_Backend-*.jar app.jar

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk --no-cache add curl

COPY --from=build /app/app.jar app.jar

# Create data directory for H2 database
RUN mkdir -p /app/data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]