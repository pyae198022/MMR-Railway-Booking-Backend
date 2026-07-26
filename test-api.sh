#!/bin/bash

# Test script for MMR Railway Booking Backend API
# Make sure the application is running on localhost:8080

echo "Testing MMR Railway Booking Backend API"
echo "========================================"

# Wait for application to start
echo "Waiting for application to start..."
sleep 5

# Test health endpoint
echo ""
echo "1. Testing health endpoint:"
curl -s http://localhost:8080/api/health | jq . || echo "Health check failed or jq not installed"

# Test info endpoint
echo ""
echo "2. Testing info endpoint:"
curl -s http://localhost:8080/api/info | jq . || echo "Info endpoint failed"

# Test stations endpoint
echo ""
echo "3. Testing stations endpoint:"
curl -s http://localhost:8080/api/stations | jq '.[0:2]' || echo "Stations endpoint failed"

# Test trains endpoint
echo ""
echo "4. Testing trains endpoint:"
curl -s http://localhost:8080/api/trains | jq '.[0:2]' || echo "Trains endpoint failed"

# Test active trains
echo ""
echo "5. Testing active trains:"
curl -s http://localhost:8080/api/trains/active | jq '.[0:2]' || echo "Active trains endpoint failed"

echo ""
echo "API Test Complete!"
echo ""
echo "Next steps:"
echo "1. Use Postman or curl to test POST endpoints"
echo "2. Check application logs for any errors"
echo "3. Verify database tables are created"
echo ""
echo "Sample curl commands for further testing:"
echo ""
echo "Search trains (Yangon to Mandalay):"
echo 'curl -X POST http://localhost:8080/api/trains/search \'
echo '  -H "Content-Type: application/json" \'
echo '  -d '\''{'
echo '    "sourceCity": "Yangon",'
echo '    "destinationCity": "Mandalay",'
echo '    "journeyDate": "2026-07-26T08:00:00",'
echo '    "numberOfPassengers": 2'
echo '  }'\'''
echo ""
echo "Create booking (Myanmar context):"
echo 'curl -X POST http://localhost:8080/api/bookings \'
echo '  -H "Content-Type: application/json" \'
echo '  -d '\''{'
echo '    "trainId": 1,'
echo '    "userId": 2,'
echo '    "sourceStationId": 1,'
echo '    "destinationStationId": 2,'
echo '    "journeyDate": "2026-07-26T08:00:00",'
echo '    "paymentMethod": "KBZ_PAY",'
echo '    "passengers": ['
echo '      {'
echo '        "firstName": "Min",'
echo '        "lastName": "Thu",'
echo '        "age": 30,'
echo '        "gender": "MALE",'
echo '        "idType": "NRC",'
echo '        "idNumber": "12/YGN(N)123456",'
echo '        "dateOfBirth": "1994-01-15",'
echo '        "berthPreference": "LOWER"'
echo '      }'
echo '    ]'
echo '  }'\'''