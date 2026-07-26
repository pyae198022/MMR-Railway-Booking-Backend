# Frontend Integration Guide (Myanmar Edition)

This guide explains how to integrate the React/Vite frontend with the Myanmar Railway Booking Spring Boot backend.

## Frontend Setup

Your frontend is located at `/Users/pyaesone/Projects/mmr-railway-booking` and appears to be a React application with Vite.

## Configuration

### 1. Update Frontend API Base URL

In your frontend, you should configure the API base URL. Common approaches:

**Option A: Environment Variable**
Create a `.env` file in your frontend project:
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Then use it in your API service:
```javascript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
```

**Option B: Configuration File**
Create a `src/config.js`:
```javascript
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8080/api',
  TIMEOUT: 10000,
};
```

### 2. CORS Configuration

The backend already has CORS enabled for all origins in development. For production, you might want to restrict it:

```java
// In CorsConfig.java
config.addAllowedOrigin("http://localhost:5173"); // Vite default
config.addAllowedOrigin("http://localhost:3000"); // React default
```

## API Integration Examples

### 1. Station Service (Frontend)
```javascript
// stations.service.js
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const stationService = {
  async getAllStations() {
    const response = await axios.get(`${API_BASE}/stations`);
    return response.data;
  },
  
  async searchStations(query) {
    const response = await axios.get(`${API_BASE}/stations/search`, {
      params: { name: query }
    });
    return response.data;
  },
  
  async getStationsByCity(city) {
    const response = await axios.get(`${API_BASE}/stations/city/${city}`);
    return response.data;
  }
};
```

### 2. Train Search Service
```javascript
// train.service.js
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const trainService = {
  async searchTrains(searchParams) {
    const response = await axios.post(`${API_BASE}/trains/search`, searchParams);
    return response.data;
  },
  
  async getTrainDetails(trainId) {
    const response = await axios.get(`${API_BASE}/trains/${trainId}`);
    return response.data;
  },
  
  async getAvailableSeats(trainId) {
    const response = await axios.get(`${API_BASE}/seats/train/${trainId}/available`);
    return response.data;
  }
};
```

### 3. Booking Service
```javascript
// booking.service.js
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const bookingService = {
  async createBooking(bookingData) {
    const response = await axios.post(`${API_BASE}/bookings`, bookingData);
    return response.data;
  },
  
  async getBookingByPnr(pnr) {
    const response = await axios.get(`${API_BASE}/bookings/pnr/${pnr}`);
    return response.data;
  },
  
  async getUserBookings(userId) {
    const response = await axios.get(`${API_BASE}/bookings/user/${userId}`);
    return response.data;
  },
  
  async cancelBooking(bookingId) {
    const response = await axios.patch(`${API_BASE}/bookings/${bookingId}/cancel`);
    return response.data;
  }
};
```

## Complete Integration Example

### Search Component
```javascript
// SearchForm.jsx
import React, { useState } from 'react';
import { trainService } from '../services/train.service';
import { stationService } from '../services/station.service';

const SearchForm = () => {
  const [searchParams, setSearchParams] = useState({
    sourceCity: '',
    destinationCity: '',
    journeyDate: '',
    numberOfPassengers: 1
  });
  const [trains, setTrains] = useState([]);
  const [stations, setStations] = useState([]);
  
  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const results = await trainService.searchTrains(searchParams);
      setTrains(results);
    } catch (error) {
      console.error('Search failed:', error);
    }
  };
  
  const loadStations = async () => {
    const stations = await stationService.getAllStations();
    setStations(stations);
  };
  
  // Load stations on component mount
  React.useEffect(() => {
    loadStations();
  }, []);
  
  return (
    <form onSubmit={handleSearch}>
      {/* Form fields for source, destination, date, passengers */}
      <button type="submit">Search Trains</button>
      
      {/* Display search results */}
      {trains.map(train => (
        <div key={train.id} className="train-card">
          <h3>{train.trainName} ({train.trainNumber})</h3>
          <p>Departure: {train.departureTime}</p>
          <p>Arrival: {train.arrivalTime}</p>
          <p>Available Seats: {train.availableSeats}</p>
          <p>Price: ${train.basePrice}</p>
          <button onClick={() => bookTrain(train.id)}>Book Now</button>
        </div>
      ))}
    </form>
  );
};
```

### Booking Flow
1. **Search Trains** → `POST /api/trains/search`
2. **Select Train** → `GET /api/trains/{id}`
3. **Select Seats** → `GET /api/seats/train/{id}/available`
4. **Passenger Details** → Local form
5. **Payment** → Local simulation
6. **Confirm Booking** → `POST /api/bookings`
7. **Show Confirmation** → `GET /api/bookings/pnr/{pnr}`

## Error Handling

```javascript
// error-handler.js
export const handleApiError = (error) => {
  if (error.response) {
    // Server responded with error
    switch (error.response.status) {
      case 400:
        return 'Invalid request data';
      case 401:
        return 'Please login first';
      case 404:
        return 'Resource not found';
      case 409:
        return 'Seat already booked';
      case 500:
        return 'Server error, please try again';
      default:
        return 'An error occurred';
    }
  } else if (error.request) {
    // No response received
    return 'Network error, please check connection';
  } else {
    // Request setup error
    return 'Request configuration error';
  }
};
```

## Testing Integration

1. **Start Backend**: `mvn spring-boot:run`
2. **Start Frontend**: `npm run dev`
3. **Test API Connection**: Use the browser console to test API calls
4. **Monitor Network**: Check browser DevTools Network tab

## Production Deployment

### Backend (Spring Boot)
- Build JAR: `mvn clean package`
- Run: `java -jar target/*.jar`
- Configure production database
- Set proper CORS origins
- Enable HTTPS

### Frontend (React/Vite)
- Build: `npm run build`
- Serve static files with Nginx/Apache
- Configure API URL for production
- Enable CORS if needed

## Common Issues & Solutions

### 1. CORS Errors
**Symptom**: Browser blocks API requests
**Solution**: Ensure backend CORS config includes frontend origin

### 2. Network Errors
**Symptom**: Can't connect to backend
**Solution**: 
- Verify backend is running: `curl http://localhost:8080/api/health`
- Check firewall/port settings

### 3. API Timeout
**Symptom**: Requests timeout
**Solution**: 
- Increase timeout in axios config
- Check backend performance

### 4. Data Format Mismatch
**Symptom**: API returns 400 errors
**Solution**: 
- Verify request body format matches API expectations
- Check date formats (ISO 8601 recommended)

## Next Steps

1. **Authentication**: Implement JWT tokens
2. **Real-time Updates**: Add WebSocket for seat availability
3. **Payment Integration**: Connect to payment gateway
4. **Email Notifications**: Send booking confirmations
5. **Admin Dashboard**: Build management interface

## Support

For integration issues:
1. Check backend logs: `tail -f logs/application.log`
2. Test API directly: `curl http://localhost:8080/api/health`
3. Verify database connection
4. Check network connectivity between services