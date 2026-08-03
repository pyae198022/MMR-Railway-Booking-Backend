package com.ticket.booking.config;

import com.ticket.booking.model.*;
import com.ticket.booking.repository.*;
import com.ticket.booking.service.MockRouteService;
import com.ticket.booking.service.RouteStopService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SimpleDataLoader {
    
    @Bean
    public CommandLineRunner loadData(StationRepository stationRepository, TrainRepository trainRepository,
                                     RouteRepository routeRepository, RouteStopRepository routeStopRepository,
                                     MockRouteService mockRouteService, RouteStopService routeStopService) {
        return args -> {
            System.out.println("\n================================================");
            System.out.println(" Myanmar Railway Booking Backend Initializing...");
            System.out.println("================================================\n");
            
            // Clear existing data in correct order (reverse of dependencies)
            System.out.println("🧹 Clearing existing data...");
            
            // 1. Delete RouteStops first (depends on Routes and Stations)
            if (routeStopRepository != null) {
                routeStopRepository.deleteAll();
                System.out.println("  • RouteStops cleared");
            }
            
            // 2. Delete Routes (depends on Stations)
            routeRepository.deleteAll();
            System.out.println("  • Routes cleared");
            
            // 3. Delete Trains (depends on Stations)
            trainRepository.deleteAll();
            System.out.println("  • Trains cleared");
            
            // 4. Delete Stations (no dependencies)
            stationRepository.deleteAll();
            System.out.println("  • Stations cleared");
            
            // Load Myanmar Railway Stations (based on ort.railways.gov.mm)
            List<Station> stations = Arrays.asList(
                // Yangon Region Stations
                new Station(null, "YGN", "Yangon Central Railway Station", "Yangon", "Yangon", "8", "Ticketing, Waiting Hall, Food Court, Restrooms, Parking, CCTV"),
                new Station(null, "INS", "Insein Railway Station", "Yangon", "Yangon", "3", "Ticketing, Waiting Area, Restrooms"),
                new Station(null, "BGN", "Bago Railway Station", "Bago", "Bago", "3", "Ticketing, Waiting Hall, Restrooms"),   // idx 2
                new Station(null, "PYA", "Pyay Railway Station", "Pyay", "Bago", "3", "Ticketing, Waiting Area"),              // idx 3
                new Station(null, "TGO", "Taungoo Railway Station", "Taungoo", "Bago", "2", "Basic Facilities"),              // idx 4
                new Station(null, "MDY", "Mandalay Railway Station", "Mandalay", "Mandalay", "6", "Ticketing, Waiting Hall, Food Stalls, Restrooms, Parking"), // idx 5
                new Station(null, "SAG", "Sagaing Railway Station", "Sagaing", "Sagaing", "3", "Ticketing, Waiting Area"),     // idx 6
                new Station(null, "MNY", "Monywa Railway Station", "Monywa", "Sagaing", "3", "Ticketing, Waiting Area"),      // idx 7
                new Station(null, "NPT", "Naypyitaw Railway Station", "Naypyitaw", "Naypyitaw", "4", "Ticketing, Modern Waiting Hall, Restrooms, Parking"), // idx 8
                new Station(null, "THZ", "Thazi Railway Junction", "Thazi", "Mandalay", "4", "Ticketing, Junction Station, Restrooms, Transfer Point"),      // idx 9
                new Station(null, "SHW", "Shwenyaung Railway Station", "Shwenyaung", "Shan State", "2", "Basic Facilities"),  // idx 10
                new Station(null, "HTY", "Htaukkyant Railway Station", "Htaukkyant", "Yangon", "2", "Basic Facilities"),      // idx 11
                new Station(null, "PHU", "Pyin Oo Lwin Railway Station", "Pyin Oo Lwin", "Mandalay", "3", "Ticketing, Scenic Station, Restrooms"), // idx 12
                new Station(null, "LSK", "Lashio Railway Station", "Lashio", "Shan State", "3", "Ticketing, Waiting Area"),   // idx 13
                new Station(null, "KYT", "Kyaukse Railway Station", "Kyaukse", "Mandalay", "2", "Basic Facilities"),          // idx 14
                new Station(null, "KLA", "Kalay Railway Station", "Kalay", "Sagaing", "3", "Ticketing, Waiting Area"),        // idx 15
                new Station(null, "MYK", "Myitkyina Railway Station", "Myitkyina", "Kachin State", "4", "Ticketing, Waiting Hall, Restrooms"), // idx 16
                new Station(null, "HPA", "Hpa-An Railway Station", "Hpa-An", "Kayin State", "2", "Basic Facilities"),         // idx 17
                new Station(null, "MAW", "Mawlamyine Railway Station", "Mawlamyine", "Mon State", "4", "Ticketing, Waiting Hall, Restrooms"),  // idx 18
                new Station(null, "TNY", "Taunggyi Railway Station", "Taunggyi", "Shan State", "3", "Ticketing, Waiting Area"),// idx 19
                // Additional stations for expanded routes
                new Station(null, "KLW", "Kalaw Railway Station", "Kalaw", "Shan State", "2", "Ticketing, Hill Station, Scenic Views, Cool Climate"), // idx 20
                new Station(null, "BGP", "Bagan Railway Station", "Bagan", "Mandalay", "2", "Ticketing, Tourist Station, Temple Views"),              // idx 21
                new Station(null, "SWB", "Shwebo Railway Station", "Shwebo", "Sagaing", "3", "Ticketing, Waiting Area"),                              // idx 22
                new Station(null, "MEK", "Meiktila Railway Station", "Meiktila", "Mandalay", "3", "Ticketing, Junction, Waiting Area"),               // idx 23
                new Station(null, "DAW", "Dawei Railway Station", "Dawei", "Tanintharyi", "2", "Basic Facilities"),                                   // idx 24
                new Station(null, "PTH", "Pathein Railway Station", "Pathein", "Ayeyarwady", "3", "Ticketing, Waiting Area")                          // idx 25
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
            train3.setSourceStation(stations.get(5)); // Mandalay (idx 5)
            train3.setDestinationStation(stations.get(2)); // Bago (idx 2)
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
            train4.setDestinationStation(stations.get(18)); // Mawlamyine (idx 18)
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
            train5.setSourceStation(stations.get(5)); // Mandalay (idx 5)
            train5.setDestinationStation(stations.get(16)); // Myitkyina (idx 16)
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
            train6.setSourceStation(stations.get(8)); // Naypyitaw (idx 8)
            train6.setDestinationStation(stations.get(5)); // Mandalay (idx 5)
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
            train7.setDestinationStation(stations.get(12)); // Pyin Oo Lwin (idx 12)
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
            train8.setSourceStation(stations.get(2)); // Bago (idx 2)
            train8.setDestinationStation(stations.get(3)); // Pyay (idx 3)
            train8.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            train8.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(30));
            train8.setTotalSeats(100);
            train8.setAvailableSeats(80);
            train8.setBasePrice(5000.0);
            train8.setTrainType("Local");
            train8.setStatus("ACTIVE");

            trainRepository.saveAll(Arrays.asList(train6, train7, train8));
            System.out.println("✅ Added 3 more trains for popular routes");

            // DEMU and other additional train types
            Train trainDEMU1 = new Train();
            trainDEMU1.setTrainNumber("UP-93");
            trainDEMU1.setTrainName("Yangon-Bago DEMU");
            trainDEMU1.setSourceStation(stations.get(0)); // Yangon
            trainDEMU1.setDestinationStation(stations.get(2)); // Bago (idx 2)
            trainDEMU1.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(6).withMinute(0));
            trainDEMU1.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(30));
            trainDEMU1.setTotalSeats(200);
            trainDEMU1.setAvailableSeats(180);
            trainDEMU1.setBasePrice(3500.0);
            trainDEMU1.setTrainType("DEMU");
            trainDEMU1.setStatus("ACTIVE");

            Train trainDEMU2 = new Train();
            trainDEMU2.setTrainNumber("UP-95");
            trainDEMU2.setTrainName("Naypyitaw-Mandalay DEMU");
            trainDEMU2.setSourceStation(stations.get(8)); // Naypyitaw (idx 8)
            trainDEMU2.setDestinationStation(stations.get(5)); // Mandalay (idx 5)
            trainDEMU2.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0));
            trainDEMU2.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));
            trainDEMU2.setTotalSeats(200);
            trainDEMU2.setAvailableSeats(150);
            trainDEMU2.setBasePrice(6500.0);
            trainDEMU2.setTrainType("DEMU");
            trainDEMU2.setStatus("ACTIVE");

            Train trainNight = new Train();
            trainNight.setTrainNumber("UP-111");
            trainNight.setTrainName("Night Express (Yangon-Mandalay)");
            trainNight.setSourceStation(stations.get(0)); // Yangon
            trainNight.setDestinationStation(stations.get(5)); // Mandalay (idx 5)
            trainNight.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(21).withMinute(0));
            trainNight.setArrivalTime(LocalDateTime.now().plusDays(2).withHour(7).withMinute(30));
            trainNight.setTotalSeats(280);
            trainNight.setAvailableSeats(240);
            trainNight.setBasePrice(18000.0);
            trainNight.setTrainType("Night");
            trainNight.setStatus("ACTIVE");

            Train trainMail = new Train();
            trainMail.setTrainNumber("UP-35");
            trainMail.setTrainName("Lashio Mail");
            trainMail.setSourceStation(stations.get(5)); // Mandalay (idx 5)
            trainMail.setDestinationStation(stations.get(13)); // Lashio (idx 13)
            trainMail.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(6).withMinute(0));
            trainMail.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
            trainMail.setTotalSeats(180);
            trainMail.setAvailableSeats(160);
            trainMail.setBasePrice(8500.0);
            trainMail.setTrainType("Mail");
            trainMail.setStatus("ACTIVE");

            Train trainOrdinary = new Train();
            trainOrdinary.setTrainNumber("UP-61");
            trainOrdinary.setTrainName("Pyay Ordinary");
            trainOrdinary.setSourceStation(stations.get(2)); // Bago (idx 2)
            trainOrdinary.setDestinationStation(stations.get(3)); // Pyay (idx 3)
            trainOrdinary.setDepartureTime(LocalDateTime.now().plusDays(1).withHour(7).withMinute(0));
            trainOrdinary.setArrivalTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));
            trainOrdinary.setTotalSeats(250);
            trainOrdinary.setAvailableSeats(200);
            trainOrdinary.setBasePrice(4000.0);
            trainOrdinary.setTrainType("Ordinary");
            trainOrdinary.setStatus("ACTIVE");

            trainRepository.saveAll(Arrays.asList(trainDEMU1, trainDEMU2, trainNight, trainMail, trainOrdinary));
            System.out.println("✅ Added DEMU, Night, Mail, and Ordinary trains");
            
            // Create sample routes for Myanmar Railways
            System.out.println("\n📡 Creating Myanmar Railway Routes...");
            
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
            
            // Create route stops for each route
            System.out.println("\n📍 Creating Route Stops...");
            
            // Define stop configurations for each route
            Map<String, List<String>> routeStopConfigs = new HashMap<>();
            
            // Yangon-Naypyitaw stops
            routeStopConfigs.put("YGN-NPT-001", Arrays.asList("YGN", "BGN", "PYA", "TGO", "NPT"));

            // Yangon-Mandalay stops
            routeStopConfigs.put("YGN-MDY-001", Arrays.asList("YGN", "BGN", "PYA", "TGO", "NPT", "THZ", "MDY"));

            // Yangon-Mawlamyine stops
            routeStopConfigs.put("YGN-MAW-001", Arrays.asList("YGN", "BGN", "KYT", "MAW"));

            // Mandalay-Bago stops
            routeStopConfigs.put("MDY-BGN-001", Arrays.asList("MDY", "THZ", "NPT", "TGO", "PYA", "BGN"));

            // Mandalay-Myitkyina stops
            routeStopConfigs.put("MDY-MYK-001", Arrays.asList("MDY", "SAG", "MNY", "KLA", "MYK"));

            // Naypyitaw-Mandalay stops
            routeStopConfigs.put("NPT-MDY-001", Arrays.asList("NPT", "THZ", "MDY"));

            // Bago-Pyay stops
            routeStopConfigs.put("BGN-PYA-001", Arrays.asList("BGN", "PYA"));

            // Yangon-Pyin Oo Lwin stops
            routeStopConfigs.put("YGN-PHU-001", Arrays.asList("YGN", "BGN", "PYA", "TGO", "NPT", "THZ", "MDY", "KYT", "PHU"));
            
            int totalStopsCreated = 0;
            
            for (Route route : routes) {
                List<String> stopCodes = routeStopConfigs.get(route.getRouteCode());
                if (stopCodes != null) {
                    // Find stations by code
                    Map<String, Station> stationByCode = new HashMap<>();
                    for (String code : stopCodes) {
                        stationRepository.findByCode(code).ifPresent(station -> stationByCode.put(code, station));
                    }
                    
                    // Create stops in order
                    int order = 1;
                    int accumulatedDistance = 0;
                    int accumulatedTime = 0;
                    double accumulatedFare = 0.0;
                    int totalDistance = route.getDistanceKm();
                    int totalTime = route.getEstimatedTravelTime();
                    double totalFare = route.getBaseFare();
                    
                    for (String code : stopCodes) {
                        Station station = stationByCode.get(code);
                        if (station != null) {
                            RouteStop routeStop = new RouteStop();
                            routeStop.setRoute(route);
                            routeStop.setStation(station);
                            routeStop.setStopOrder(order);
                            
                            // Calculate proportional distance, time, and fare
                            double proportion = (double) order / stopCodes.size();
                            routeStop.setDistanceFromStart((int) (totalDistance * proportion));
                            routeStop.setEstimatedArrivalOffset((int) (totalTime * proportion));
                            
                            // Stop duration: 5-15 minutes for intermediate stops, 0 for start/end
                            int stopDuration = (order > 1 && order < stopCodes.size()) ? 5 + (order % 10) : 0;
                            routeStop.setStopDuration(stopDuration);
                            routeStop.setEstimatedDepartureOffset((int) (totalTime * proportion) + stopDuration);
                            
                            // Platform number
                            routeStop.setPlatformNumber(String.valueOf((order % 3) + 1));
                            
                            // Stop type
                            if (order == 1 || order == stopCodes.size()) {
                                routeStop.setStopType("TERMINAL");
                                routeStop.setIsIntermediateStop(false);
                            } else if (order == 2 || order == stopCodes.size() - 1) {
                                routeStop.setStopType("MAJOR");
                                routeStop.setIsIntermediateStop(true);
                            } else {
                                routeStop.setStopType("REGULAR");
                                routeStop.setIsIntermediateStop(true);
                            }
                            
                            // Facilities
                            routeStop.setFacilitiesAvailable(station.getFacilities());
                            routeStop.setStatus("ACTIVE");
                            
                            // Calculate fare from start
                            routeStop.setStopFareFromStart(totalFare * proportion);
                            
                            routeStopRepository.save(routeStop);
                            totalStopsCreated++;
                            
                            order++;
                        }
                    }
                    
                    System.out.println("  • " + route.getRouteCode() + ": " + stopCodes.size() + " stops created");
                }
            }
            
            System.out.println("✅ Created " + totalStopsCreated + " Route Stops");
            
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
            System.out.println("- Route Stops: " + routeStopRepository.count());
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