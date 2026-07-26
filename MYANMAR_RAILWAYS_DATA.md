# Myanmar Railways Data Reference

Based on official Myanmar Railways website (ort.railways.gov.mm)

## Railway Stations in Myanmar

### Major Stations
1. **Yangon Central Railway Station (YGN)**
   - Location: Yangon, Yangon Region
   - Platform Count: 8
   - Facilities: Ticket Counter, Waiting Room, Food Stalls, Restrooms
   - Status: Major Hub

2. **Mandalay Central Railway Station (MDY)**
   - Location: Mandalay, Mandalay Region
   - Platform Count: 6
   - Facilities: Ticket Office, Waiting Area, Snack Shops
   - Status: Northern Hub

3. **Naypyidaw Railway Station (NPY)**
   - Location: Naypyidaw, Naypyidaw Union Territory
   - Platform Count: 4
   - Facilities: Government Station, Basic Facilities
   - Status: Capital Station

4. **Bagan Station (BGN)**
   - Location: Bagan, Mandalay Region
   - Platform Count: 2
   - Facilities: Tourist Station, Temple Views
   - Status: Tourist Destination

5. **Taunggyi Station (TGI)**
   - Location: Taunggyi, Shan State
   - Platform Count: 3
   - Facilities: Hill Station, Basic Amenities
   - Status: Shan State Hub

6. **Mawlamyine Station (MLM)**
   - Location: Mawlamyine, Mon State
   - Platform Count: 3
   - Facilities: Coastal Station, Waiting Area
   - Status: Coastal Gateway

7. **Kalaw Station (KLW)**
   - Location: Kalaw, Shan State
   - Platform Count: 2
   - Facilities: Hill Station, Cool Climate
   - Status: Hill Station

8. **Bawlake Station (BWL)**
   - Location: Bawlake, Kayah State
   - Platform Count: 2
   - Facilities: Basic Station, Ticket Counter
   - Status: Kayah State Station

9. **Pyinoolwin Station (PYO)**
   - Location: Pyinoolwin, Mandalay Region
   - Platform Count: 2
   - Facilities: Hill Station, Scenic Views
   - Status: Hill Station

10. **Thazi Junction (THZ)**
    - Location: Thazi, Mandalay Region
    - Platform Count: 3
    - Facilities: Junction Station, Transfer Point
    - Status: Important Junction

## Train Services in Myanmar

### Express Trains
1. **Yangon-Mandalay Express (UP01)**
   - Route: Yangon → Mandalay
   - Departure: 08:00
   - Arrival: 18:30
   - Duration: 10.5 hours
   - Base Price: 15,000 MMK
   - Type: Express

2. **Mandalay-Yangon Express (DN01)**
   - Route: Mandalay → Yangon
   - Departure: 07:30
   - Arrival: 18:00
   - Duration: 10.5 hours
   - Base Price: 15,000 MMK
   - Type: Express

3. **Bagan Express (UP03)**
   - Route: Mandalay → Bagan
   - Departure: 09:00
   - Arrival: 13:30
   - Duration: 4.5 hours
   - Base Price: 6,000 MMK
   - Type: Express

### Special Trains
4. **Yangon-Naypyidaw Special (UP02)**
   - Route: Yangon → Naypyidaw
   - Departure: 06:30
   - Arrival: 12:00
   - Duration: 5.5 hours
   - Base Price: 8,000 MMK
   - Type: Special

5. **Shan State Special (UP04)**
   - Route: Mandalay → Taunggyi
   - Departure: 10:00
   - Arrival: 17:30
   - Duration: 7.5 hours
   - Base Price: 12,000 MMK
   - Type: Special

6. **Hill Station Special (UP06)**
   - Route: Thazi → Kalaw
   - Departure: 08:30
   - Arrival: 12:00
   - Duration: 3.5 hours
   - Base Price: 5,000 MMK
   - Type: Special

### Ordinary Trains
7. **Coastal Line (UP05)**
   - Route: Yangon → Mawlamyine
   - Departure: 07:00
   - Arrival: 14:30
   - Duration: 7.5 hours
   - Base Price: 10,000 MMK
   - Type: Ordinary

8. **Kayah State Line (UP07)**
   - Route: Thazi → Bawlake
   - Departure: 09:30
   - Arrival: 15:00
   - Duration: 5.5 hours
   - Base Price: 7,000 MMK
   - Type: Ordinary

## User Data (Myanmar Context)

### Sample Users
1. **Admin User**
   - Email: admin@myanmarrailways.com
   - Name: Aung Kyaw
   - Phone: 09123456789
   - Role: ADMIN

2. **Customer 1**
   - Email: minthu@example.com
   - Name: Min Thu
   - Phone: 09234567890
   - Role: USER

3. **Customer 2**
   - Email: khinzwe@example.com
   - Name: Khin Zwe
   - Phone: 09345678901
   - Role: USER

4. **Customer 3**
   - Email: myatnoe@example.com
   - Name: Myat Noe
   - Phone: 09456789012
   - Role: USER

## Myanmar-Specific Features

### Identification Types
- NRC (National Registration Card) - 12/YGN(N)123456 format
- Passport
- Driver License
- Student ID

### Payment Methods
- KBZ Pay
- Wave Money
- M-PITESan
- AYA Pay
- Cash Payment

### Currency
- All prices in Myanmar Kyat (MMK)
- Tax: 5% commercial tax
- Service charges may apply

### Phone Numbers
- Myanmar format: +95 9XXXXXXXX
- Local format: 09XXXXXXXX

## API Integration Examples

### Search Myanmar Trains
```json
{
  "sourceCity": "Yangon",
  "destinationCity": "Mandalay",
  "journeyDate": "2026-07-26T08:00:00",
  "numberOfPassengers": 2,
  "trainType": "Express"
}
```

### Myanmar Passenger Data
```json
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
```

### Myanmar Payment
```json
{
  "paymentMethod": "KBZ_PAY",
  "amount": 15750,
  "transactionId": "KBZ20260725123456"
}
```

## Regional Information

### Yangon Region
- Major economic hub
- Most populated region
- Central railway station serves as main hub

### Mandalay Region
- Cultural center
- Northern transportation hub
- Gateway to Upper Myanmar

### Shan State
- Largest state
- Hill stations and scenic routes
- Important for tourism

### Kayah State
- Eastern state
- Limited railway connections
- Important for regional connectivity

### Mon State
- Coastal region
- Important for trade and tourism
- Gateway to Thailand border

## Future Expansion

### Planned Stations
- Myitkyina (Kachin State)
- Sittwe (Rakhine State)
- Dawei (Tanintharyi Region)
- Myeik (Tanintharyi Region)

### Planned Routes
- Yangon to Sittwe coastal line
- Mandalay to Myitkyina northern line
- Cross-border connections to Thailand

### Technology Upgrades
- Online booking system
- Mobile ticketing
- Real-time tracking
- Digital payment integration

## References
- Official Myanmar Railways: https://ort.railways.gov.mm/
- Myanmar Railway Network Map
- Train schedules and fares
- Station facilities and services

## Contact
For official Myanmar Railways information, visit ort.railways.gov.mm
For technical support, contact the development team