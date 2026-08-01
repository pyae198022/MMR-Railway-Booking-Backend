package com.ticket.booking.config;

import com.ticket.booking.model.Route;
import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import com.ticket.booking.repository.RouteRepository;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.repository.TrainRepository;
import com.ticket.booking.service.MockRouteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SimpleDataLoader {
    
    @Bean
    public CommandLineRunner loadData(StationRepository stationRepository, TrainRepository trainRepository,
                                     RouteRepository routeRepository, MockRouteService mockRouteService) {
        return args -> {
            System.out.println("\n================================================");
            System.out.println(" Myanmar Railway Booking Backend Initializing...");
            System.out.println("================================================\n");
            
            // Clear existing data
            trainRepository.deleteAll();
            stationRepository.deleteAll();
            
            // Load Myanmar Railway Stations (based on ort.railways.gov.mm)
            List<Station> stations = Arrays.asList(
                // Yangon Region Stations
                new Station(null, "YGN", "Yangon Central Railway Station", "Yangon", "Yangon", "8", "Ticketing, Waiting Hall, Food Court, Restrooms, Parking, CCTV"),
                new Station(null, "INS", "Insein Railway Station", "Yangon", "Yangon", "3", "Ticketing, Waiting Area, Restrooms"),
                new Station(null, "BGN", "Bago Railway Station", "Bago", "Bago", "3", "Ticketing, Waiting Hall, Restrooms"),
                new Station(null, "PYA", "Pyay Railway Station", "Pyay", "Bago", "3", "Ticketing, Waiting Area"),
                new Station(null, "TGO", "Taungoo Railway Station", "Taungoo", "Bago", "2", "Basic Facilities"),
                new Station(null, "MDY", "Mandalay Railway Station", "Mandalay", "Mandalay", "6", "Ticketing, Waiting Hall, Food Stalls, Restrooms, Parking"),
                new Station(null, "SAG", "Sagaing Railway Station", "Sagaing", "Sagaing", "3", "Ticketing, Waiting Area"),
                new Station(null, "MNY", "Monywa Railway Station", "Monywa", "Sagaing", "3", "Ticketing, Waiting Area"),
                new Station(null, "NPT", "Naypyitaw Railway Station", "Naypyitaw", "Naypyitaw", "4", "Ticketing, Modern Waiting Hall, Restrooms, Parking"),
                new Station(null, "THT", "Thazi Railway Station", "Thazi", "Mandalay", "4", "Ticketing, Junction Station, Restrooms"),
                new Station(null, "SHW", "Shwenyaung Railway Station", "Shwenyaung", "Shan State", "2", "Basic Facilities"),
                new Station(null, "HTY", "Htaukkyant Railway Station", "Htaukkyant", "Yangon", "2", "Basic Facilities"),
                new Station(null, "PHU", "Pyin Oo Lwin Railway Station", "Pyin Oo Lwin", "Mandalay", "3", "Ticketing, Scenic Station, Restrooms"),
                new Station(null, "LSK", "Lashio Railway Station", "Lashio", "Shan State", "3", "Ticketing, Waiting Area"),
                new Station(null, "KYT", "Kyaukse Railway Station", "Kyaukse", "Mandalay", "2", "Basic Facilities"),
                new Station(null, "KLA", "Kalay Railway Station", "Kalay", "Sagaing", "3", "Ticketing, Waiting Area"),
                new Station(null, "MYK", "Myitkyina Railway Station", "Myitkyina", "Kachin State", "4", "Ticketing, Waiting Hall, Restrooms"),
                new Station(null, "HPA", "Hpa-An Railway Station", "Hpa-An", "Kayin State", "2", "Basic Facilities"),
                new Station(null, "MAW", "Mawlamyine Railway Station", "Mawlamyine", "Mon State", "4", "Ticketing, Waiting Hall, Restrooms"),
                new Station(null, "TNY", "Taunggyi Railway Station", "Taunggyi", "Shan State", "3", "Ticketing, Waiting Area")
            );
            
            stationRepository.saveAll(stations);
            System.out.println("✅ Loaded " + stations.size() + " Myanmar Railway Stations");
            
            // Create sample trains based on popular routes in Myanmar
            Train train1 = new Train();
            train1.setTrainNumber("TR-001");
            train1.setTrainName("Yangon-Mandalay Express");
            train1.setSourceStation(stations.get(0)); // Yangon
            train1.setDestinationStation(stations.get(5)); // Mandalay
            train1.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(7).withMinute(0));
            train1.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(17).withMinute(30));
            train1.setTotalSeats(200);
            train1.setAvailableSeats(180);
            train1.setBasePrice(15000.0);
            train1.setTrainType("Express");
            train1.setStatus("ACTIVE");
            
            Train train2 = new Train();
            train2.setTrainNumber("TR-002");
            train2.setTrainName("Yangon-Naypyitaw Special");
            train2.setSourceStation(stations.get(0)); // Yangon
            train2.setDestinationStation(stations.get(8)); // Naypyitaw
            train2.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(30));
            train2.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(13).withMinute(0));
            train2.setTotalSeats(180);
            train2.setAvailableSeats(150);
            train2.setBasePrice(8000.0);
            train2.setTrainType("Special");
            train2.setStatus("ACTIVE");
            
            Train train3 = new Train();
            train3.setTrainNumber("TR-003");
            train3.setTrainName("Mandalay-Bago Local");
            train3.setSourceStation(stations.get(5)); // Mandalay
            train3.setDestinationStation(stations.get(2)); // Bago
            train3.setDepartureTime(LocalDateTime.now().plusDays(2).withHour(6).withMinute(0));
            train3.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(19).withMinute(0));
            train3.setTotalSeats(150);
            train3.setAvailableSeats(120);
            train3.setBasePrice(10000.0);
            train3.setTrainType("Local");
            train3.setStatus("ACTIVE");
            
            Train train4 = new Train();
            train4.setTrainNumber("TR-004");
            train4.setTrainName("Yangon-Mawlamyine Express");
            train4.setSourceStation(stations.get(0)); // Yangon
            train4.setDestinationStation(stations.get(18)); // Mawlamyine
            train4.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0));
            train4.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(15).withMinute(30));
            train4.setTotalSeats(180);
            train4.setAvailableSeats(160);
            train4.setBasePrice(12000.0);
            train4.setTrainType("Express");
            train4.setStatus("ACTIVE");
            
            Train train5 = new Train();
            train5.setTrainNumber("TR-005");
            train5.setTrainName("Mandalay-Myitkyina Special");
            train5.setSourceStation(stations.get(5)); // Mandalay
            train5.setDestinationStation(stations.get(16)); // Myitkyina
            train5.setDepartureTime(LocalDateTime.now().plusDays(2).withHour(7).withMinute(30));
            train5.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(20).withMinute(0));
            train5.setTotalSeats(160);
            train5.setAvailableSeats(140);
            train5.setBasePrice(18000.0);
            train5.setTrainType("Special");
            train5.setStatus("ACTIVE");
            
            List<Train> trains = Arrays.asList(train1, train2, train3, train4, train5);
            trainRepository.saveAll(trains);
            System.out.println("✅ Loaded " + trains.size() + " Myanmar Railway Trains");
            
            // Create more trains for different routes
            Train train6 = new Train();
            train6.setTrainNumber("TR-006");
            train6.setTrainName("Naypyitaw-Mandalay Express");
            train6.setSourceStation(stations.get(8)); // Naypyitaw
            train6.setDestinationStation(stations.get(5)); // Mandalay
            train6.setDepartureTime(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0));
            train6.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(18).withMinute(30));
            train6.setTotalSeats(200);
            train6.setAvailableSeats(170);
            train6.setBasePrice(9000.0);
            train6.setTrainType("Express");
            train6.setStatus("ACTIVE");
            
            Train train7 = new Train();
            train7.setTrainNumber("TR-007");
            train7.setTrainName("Yangon-Pyin Oo Lwin Scenic");
            train7.setSourceStation(stations.get(0)); // Yangon
            train7.setDestinationStation(stations.get(12)); // Pyin Oo Lwin
            train7.setDepartureTime(LocalDateTime.now().plusDays(3).withHour(6).withMinute(30));
            train7.setArrivalTime(LocalDateTime.now().plusDays(3).withHour(19).withMinute(0));
            train7.setTotalSeats(120);
            train7.setAvailableSeats(100);
            train7.setBasePrice(25000.0);
            train7.setTrainType("Scenic");
            train7.setStatus("ACTIVE");
            
            Train train8 = new Train();
            train8.setTrainNumber("TR-008");
            train8.setTrainName("Bago-Pyay Local");
            train8.setSourceStation(stations.get(2)); // Bago
            train8.setDestinationStation(stations.get(3)); // Pyay
            train8.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            train8.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(30));
            train8.setTotalSeats(100);
            train8.setAvailableSeats(80);
            train8.setBasePrice(5000.0);
            train8.setTrainType("Local");
            train8.setStatus("ACTIVE");
            
            trainRepository.saveAll(Arrays.asList(train6, train7, train8));
            System.out.println("✅ Added 3 more trains for popular routes");
            
            // Create sample routes for Myanmar Railways
            System.out.println("\n📡 Creating Myanmar Railway Routes...");
            
            // Clear existing routes first
            routeRepository.deleteAll();
            
            // Create routes between major stations
            List<Route> routes = Arrays.asList(
                // Yangon to Naypyitaw (Very important route!)
                new Route(null, "YGN-NPT-001", "Yangon-Naypyitaw Main Line", 
                    stations.get(0), stations.get(8), // YGN to NPT
                    320, 240, "Main", 
                    "Primary route connecting commercial capital to administrative capital",
                    "ACTIVE", 8000.0, null),
                
                // Yangon to Mandalay
                new Route(null, "YGN-MDY-001", "Yangon-Mandalay Main Line",
                    stations.get(0), stations.get(5), // YGN to MDY
                    620, 630, "Main",
                    "Major north-south corridor connecting two largest cities",
                    "ACTIVE", 15000.0, null),
                
                // Yangon to Mawlamyine
                new Route(null, "YGN-MAW-001", "Yangon-Mawlamyine Coastal Line",
                    stations.get(0), stations.get(18), // YGN to MAW
                    300, 450, "Coastal",
                    "Scenic coastal route to Mon State capital",
                    "ACTIVE", 12000.0, null),
                
                // Mandalay to Bago
                new Route(null, "MDY-BGN-001", "Mandalay-Bago Central Line",
                    stations.get(5), stations.get(2), // MDY to BGN
                    520, 780, "Main",
                    "Central Myanmar route connecting ancient capitals",
                    "ACTIVE", 10000.0, null),
                
                // Mandalay to Myitkyina
                new Route(null, "MDY-MYK-001", "Mandalay-Myitkyina Northern Line",
                    stations.get(5), stations.get(16), // MDY to MYK
                    780, 750, "Northern",
                    "Northern route to Kachin State capital",
                    "ACTIVE", 18000.0, null),
                
                // Naypyitaw to Mandalay
                new Route(null, "NPT-MDY-001", "Naypyitaw-Mandalay Central Line",
                    stations.get(8), stations.get(5), // NPT to MDY
                    320, 270, "Main",
                    "Connecting administrative capital to cultural capital",
                    "ACTIVE", 9000.0, null),
                
                // Bago to Pyay
                new Route(null, "BGN-PYA-001", "Bago-Pyay Western Line",
                    stations.get(2), stations.get(3), // BGN to PYA
                    180, 270, "Regional",
                    "Western regional route in Bago Region",
                    "ACTIVE", 5000.0, null),
                
                // Yangon to Pyin Oo Lwin
                new Route(null, "YGN-PHU-001", "Yangon-Pyin Oo Lwin Scenic Route",
                    stations.get(0), stations.get(12), // YGN to PHU
                    690, 750, "Scenic",
                    "Beautiful hill station route with scenic views",
                    "ACTIVE", 25000.0, null)
            );
            
            routeRepository.saveAll(routes);
            System.out.println("✅ Created " + routes.size() + " Myanmar Railway Routes");
            
            System.out.println("\n================================================");
            System.out.println(" Data Initialization Complete!");
            System.out.println("================================================\n");
            
            System.out.println("Backend API running on: http://localhost:8080");
            System.out.println("H2 Console: http://localhost:8080/h2-console");
            System.out.println("JDBC URL: jdbc:h2:mem:mmr_railway_booking");
            System.out.println("Username: sa");
            System.out.println("Password: (empty)");
            System.out.println("\nLoaded Data Summary:");
            System.out.println("- Stations: " + stationRepository.count());
            System.out.println("- Trains: " + trainRepository.count());
            System.out.println("- Routes: " + routeRepository.count());
            System.out.println("\nAvailable Endpoints:");
            System.out.println("- GET  /api/health - Health check");
            System.out.println("- GET  /api/info - API information");
            System.out.println("- GET  /api/stations - List all stations");
            System.out.println("- GET  /api/trains - List all trains");
            System.out.println("- GET  /api/routes - List all routes");
            System.out.println("- POST /api/trains/search - Search trains");
            System.out.println("- POST /api/bookings - Create booking");
            System.out.println("- GET  /api/routes/between-cities - Find routes between cities");
            System.out.println("\nFrontend should connect to: http://localhost:8080/api");
            System.out.println("\n📊 Sample Stations:");
            stations.forEach(station -> System.out.println("  - " + station.getName() + " (" + station.getCode() + ")"));
            System.out.println("\n🚂 Sample Trains:");
            trains.forEach(train -> System.out.println("  - " + train.getTrainName() + " (" + train.getTrainNumber() + ")"));
            System.out.println("\n🛤️  Sample Routes:");
            routes.forEach(route -> System.out.println("  - " + route.getRouteName() + 
                " (" + route.getStartStation().getCode() + " → " + route.getEndStation().getCode() + ") - " + 
                route.getDistanceKm() + "km, " + (route.getEstimatedTravelTime()/60) + "h"));
            System.out.println("\n================================================");
        };
    }
}