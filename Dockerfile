# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# This command compiles your Java code and outputs the jar into /app/target/ inside the container
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install curl for health checks
RUN apk --no-cache add curl

# --- FIXED: Correctly copy the jar from the "build" stage container instead of your local machine ---
COPY --from=build /app/target/MMR_Railway_Booking_Backend-*.jar app.jar

# Create data directory for H2 database
RUN mkdir -p /app/data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
