# Myanmar Railway Booking Backend API

A comprehensive Spring Boot backend system for booking trains on Myanmar Railways based on official data from ort.railways.gov.mm. This system provides RESTful APIs for station management, train search, booking, and fare calculation specific to Myanmar's railway network.

## 🚂 Features

### Myanmar Railways Data
- **20 Official Stations**: Based on Myanmar Railways official stations including Yangon Central, Mandalay, Naypyitaw, Bago, Mawlamyine, and more
- **8 Active Trains**: Realistic Myanmar train routes with schedules and pricing in MMK
- **Station Facilities**: Complete facility information for each station
- **Regional Grouping**: Stations organized by Myanmar states and regions

### Booking & Ticketing
- **Fare Calculator**: Accurate Myanmar Railways fare calculation based on distance and train type
- **Discount System**: Support for student, senior citizen, and child discounts
- **Service Charges**: Myanmar Railways service charges implementation
- **PNR Generation**: Unique booking reference numbers

### API Management
- **Stations API**: Complete CRUD for Myanmar railway stations
- **Trains API**: Train management with search capabilities
- **Booking API**: End-to-end booking flow
- **Health & Info**: System monitoring endpoints

### Integration Ready
- **CORS Configured**: Ready for frontend integration
- **Multiple Databases**: H2 for development, PostgreSQL for production
- **Security**: Basic Spring Security with customizable configuration
- **Frontend Bundled**: React frontend included in JAR deployment

## 🛠️ Technology Stack

### Backend Framework
- **Java 17** - Main programming language
- **Spring Boot 3.4.3** - REST API framework
- **Spring Data JPA** - Database operations
- **Spring Security** - Authentication & authorization
- **Spring MVC** - Web layer

### Data Layer
- **H2 Database** - In-memory for development
- **PostgreSQL** - Production database option
- **Spring Data JPA** - ORM and repositories
- **Lombok** - Boilerplate reduction

### Build & Deployment
- **Maven** - Build tool and dependency management
- **Docker** - Containerization ready
- **Spring Boot Maven Plugin** - JAR packaging

### Integration
- **RESTful APIs** - JSON-based endpoints
- **CORS Support** - Frontend integration ready
- **H2 Console** - Database management
- **Actuator** - Production monitoring

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (or Docker for containerized setup)

## Setup and Installation

### 1. Clone and Navigate
```bash
cd MMR_Railway_Booking_Backend
```

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE mmr_railway_booking;
```

Or use Docker:
```bash
docker run --name mmr-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=mmr_railway_booking -p 5432:5432 -d postgres:15
```

### 3. Configure Application
Update `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mmr_railway_booking
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 4. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Or package and run
mvn clean package
java -jar target/MMR_Railway_Booking_Backend-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Health Check
- `GET /api/health` - Application health status
- `GET /api/info` - API information

### Station Management
- `GET /api/stations` - Get all stations
- `GET /api/stations/{id}` - Get station by ID
- `GET /api/stations/code/{code}` - Get station by code
- `GET /api/stations/city/{city}` - Get stations by city
- `GET /api/stations/state/{state}` - Get stations by state
- `POST /api/stations` - Create new station
- `PUT /api/stations/{id}` - Update station
- `DELETE /api/stations/{id}` - Delete station

### Train Management
- `GET /api/trains` - Get all trains
- `GET /api/trains/{id}` - Get train by ID
- `GET /api/trains/number/{trainNumber}` - Get train by number
- `GET /api/trains/type/{trainType}` - Get trains by type
- `GET /api/trains/active` - Get active trains
- `GET /api/trains/available-seats` - Get trains with available seats
- `POST /api/trains` - Create new train
- `PUT /api/trains/{id}` - Update train
- `DELETE /api/trains/{id}` - Delete train
- `POST /api/trains/search` - Search trains (with request body)

### Booking Management
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/pnr/{pnrNumber}` - Get booking by PNR
- `GET /api/bookings/user/{userId}` - Get bookings by user
- `POST /api/bookings` - Create new booking
- `PATCH /api/bookings/{bookingId}/cancel` - Cancel booking
- `PATCH /api/bookings/{bookingId}/status` - Update booking status
- `PATCH /api/bookings/{bookingId}/payment-status` - Update payment status
- `POST /api/bookings/calculate-fare` - Calculate fare

## Sample Data (Myanmar Edition)

The application automatically seeds Myanmar railway data on startup:
- **Stations**: Yangon Central, Mandalay Central, Naypyidaw, Bagan, Taunggyi, Mawlamyine, Kalaw, Bawlake
- **Trains**: Yangon-Mandalay Express, Bagan Express, Shan State Special, Coastal Line, Hill Station Special
- **Users**: Admin and sample customers with Myanmar names and phone numbers (+95)
- **Pricing**: In Myanmar Kyat (MMK)

## Sample API Requests

### Search Trains (Myanmar Routes)
```bash
curl -X POST http://localhost:8080/api/trains/search \
  -H "Content-Type: application/json" \
  -d '{
    "sourceCity": "Yangon",
    "destinationCity": "Mandalay",
    "journeyDate": "2026-07-26T08:00:00",
    "numberOfPassengers": 2
  }'
```

### Create Booking (Myanmar Context)
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "trainId": 1,
    "userId": 2,
    "sourceStationId": 1,
    "destinationStationId": 2,
    "journeyDate": "2026-07-26T08:00:00",
    "paymentMethod": "KBZ_PAY",
    "passengers": [
      {
        "firstName": "Min",
        "lastName": "Thu",
        "age": 30,
        "gender": "MALE",
        "idType": "NRC",
        "idNumber": "12/YGN(N)123456",
        "dateOfBirth": "1994-01-15",
        "berthPreference": "LOWER"
      }
    ]
  }'
```

### Get Booking by PNR
```bash
curl http://localhost:8080/api/bookings/pnr/PNRABC123
```

## Project Structure

```
src/main/java/com/ticket/booking/
├── config/              # Configuration classes
├── controller/          # REST controllers
├── dto/                # Data Transfer Objects
├── model/              # JPA entities
├── repository/         # Spring Data repositories
├── service/            # Service interfaces
│   └── impl/          # Service implementations
└── MmrRailwayBookingBackendApplication.java
```

## Database Schema (Myanmar Context)

The application uses the following main entities customized for Myanmar:
- **Station**: Myanmar railway stations with codes and locations (Yangon, Mandalay, etc.)
- **Train**: Myanmar train details, schedules, and availability in MMK
- **User**: System users with Myanmar names and +95 phone numbers
- **Booking**: Booking records with PNR numbers
- **Passenger**: Passenger details with Myanmar NRC numbers
- **Seat**: Train seat configuration and availability
- **Payment**: Payment transaction records with Myanmar payment methods (KBZ Pay, Wave Money, etc.)

## Development

### Running Tests
```bash
mvn test
```

### Code Style
The project uses:
- Lombok annotations to reduce boilerplate
- Constructor injection with `@RequiredArgsConstructor`
- Consistent naming conventions
- Proper error handling

### Future Enhancements
1. **Authentication**: JWT-based authentication
2. **Email Notifications**: Booking confirmations
3. **Payment Gateway Integration**: Real payment processing
4. **Redis Cache**: For frequent queries
5. **WebSocket**: Real-time seat availability
6. **Admin Dashboard**: Advanced management interface

## Troubleshooting

### Common Issues

1. **Database Connection Error**: 
   - Verify PostgreSQL is running
   - Check credentials in `application.properties`

2. **Port Already in Use**:
   ```bash
   lsof -ti:8080 | xargs kill -9
   ```

3. **Build Errors**:
   ```bash
   mvn clean compile
   ```

### Logs
Check application logs for detailed information:
```bash
tail -f /path/to/logs/application.log
```

## License

This project is for educational and demonstration purposes.

## Contact

For questions or issues, please contact the development team.