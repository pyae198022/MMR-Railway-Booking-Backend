# Deployment Guide for Myanmar Railway Booking

This guide covers how to deploy the Myanmar Railway Booking application to production.

## Option 1: Single JAR (Embedded H2) - Easiest for Small Deployments

### Build the Application
```bash
cd /Users/pyaesone/Projects/MMR_Railway_Booking_Backend
mvn clean package
```

### Run the Application
```bash
java -jar target/MMR_Railway_Booking_Backend-0.0.1-SNAPSHOT.jar
```

### Access the Application
- **Frontend**: http://localhost:8080
- **Backend API**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console

### H2 Database Configuration (for production)
Update `src/main/resources/application-prod.properties`:
```properties
spring.datasource.url=jdbc:h2:file:./data/mmr_railway_booking
spring.datasource.username=sa
spring.datasource.password=
```

The database will be stored at `./data/mmr_railway_booking.mv.db` relative to where you run the JAR.

---

## Option 2: Docker Deployment

### Build Docker Image
```bash
cd /Users/pyaesone/Projects/MMR_Railway_Booking_Backend
docker build -t mmr-railway-booking .
```

### Run with Docker
```bash
docker run -p 8080:8080 --name mmr-railway-booking -d mmr-railway-booking
```

### Run with Docker Compose
```bash
cd /Users/pyaesone/Projects/MMR_Railway_Booking_Backend
docker-compose up -d
```

### View Logs
```bash
docker logs -f mmr-railway-booking
```

---

## Option 3: Production with PostgreSQL

### Setup PostgreSQL
```bash
# Using Docker
docker run --name mmr-postgres \
  -e POSTGRES_DB=mmr_railway_booking \
  -e POSTGRES_USER=mmr_user \
  -e POSTGRES_PASSWORD=secure_password \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  -d postgres:15

# Or use your existing PostgreSQL instance
```

### Configure Application
Update `src/main/resources/application-prod.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mmr_railway_booking
spring.datasource.username=mmr_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Disable H2 console in production
spring.h2.console.enabled=false
```

### Add PostgreSQL Dependency
Update `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Then rebuild:
```bash
mvn clean package -DskipTests
```

---

## Option 4: Cloud Deployment

### AWS ECS
1. Build the JAR
2. Push to Amazon ECR
3. Create ECS task definition
4. Deploy to ECS cluster

### Heroku
1. Create a `Procfile`:
```
web: java -Dserver.port=$PORT -jar target/MMR_Railway_Booking_Backend-0.0.1-SNAPSHOT.jar
```

2. Add PostgreSQL add-on
3. Push to Heroku

### Railway.app
1. Create a `railway.json`:
```json
{
  "services": {
    "postgres": {
      "type": "postgres"
    }
  }
}
```

2. Add environment variables:
```
SPRING_DATASOURCE_URL=jdbc:postgresql://...
```

---

## Environment Variables

You can override any configuration using environment variables:

| Variable | Description |
|----------|-------------|
| `SERVER_PORT` | Port to run the application (default: 8080) |
| `SPRING_DATASOURCE_URL` | Database connection string |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `SPRING_PROFILES_ACTIVE` | Active profile (prod, dev) |

Example:
```bash
java -jar app.jar \
  --server.port=8080 \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/mmr_railway_booking \
  --spring.datasource.username=postgres \
  --spring.datasource.password=postgres
```

---

## Production Checklist

- [ ] Update security configuration (enable authentication)
- [ ] Set up proper database backup strategy
- [ ] Configure SSL/HTTPS
- [ ] Set up logging and monitoring
- [ ] Configure proper CORS origins (not `*`)
- [ ] Set up health check endpoint
- [ ] Enable rate limiting
- [ ] Configure database connection pool
- [ ] Set up CDN for static assets
- [ ] Test in staging environment

---

## Monitoring and Health Check

### Health Endpoint
```bash
curl http://localhost:8080/api/health
```

Response:
```json
{
  "status": "UP",
  "service": "Myanmar Railway Booking Backend",
  "timestamp": "2026-07-26T...",
  "version": "1.0.0"
}
```

### Database Status
H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/mmr_railway_booking`
- Username: `sa`
- Password: (empty)

---

## Troubleshooting

### Application won't start
- Check Java version: `java -version` (should be 17+)
- Check if port 8080 is already in use: `lsof -i :8080`

### Database connection issues
- Verify database is running
- Check connection credentials
- Ensure database exists

### Frontend not loading
- Check if static files are in `src/main/resources/static/`
- Clear browser cache
- Check server logs

### Port already in use
```bash
# Find process using port 8080
lsof -ti :8080

# Kill it
kill -9 $(lsof -ti :8080)
```

---

## Backup Strategy

### H2 Database Backup
```bash
# Database is stored at ./data/mmr_railway_booking.mv.db
# Copy this file to backup location
cp ./data/mmr_railway_booking.mv.db ./backup/
```

### PostgreSQL Backup
```bash
pg_dump -U mmr_user -d mmr_railway_booking > backup.sql
pg_restore -U mmr_user -d mmr_railway_booking backup.sql
```

---

## Support

For deployment issues:
1. Check logs: `java -jar app.jar` or `docker logs container`
2. Verify configuration in `application-prod.properties`
3. Test database connectivity separately
4. Check firewall/port settings