package com.ticket.booking.service;

import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MockRouteService {
    
    @Autowired
    private StationRepository stationRepository;
    
    @Autowired
    private TrainRepository trainRepository;
    
    @Autowired
    private FareCalculatorService fareCalculatorService;
    
    // Train types for Myanmar Railways
    private static final List<String> TRAIN_TYPES = Arrays.asList(
        "Express", "Special", "Local", "Scenic", "Night", "Day"
    );
    
    // Train names with Myanmar-specific prefixes
    private static final List<String> TRAIN_NAME_PREFIXES = Arrays.asList(
        "မြန်မာ့မီးရထား", "ရွှေရထား", "ကျောက်မြန်မာ", "အင်းဝ", "ပုဂံ", "ရတနာ့", "ကျန်းမာ", "ဖြူးရထား", "ပဲခူး", "မော်လမြိုင်"
    );
    
    // Train suffixes
    private static final List<String> TRAIN_NAME_SUFFIXES = Arrays.asList(
        "အမြန်ရထား", "သီးသန့်ရထား", "ဒေသစည်း", "ခရီးသွား", "စီး", "စက်ရုံ", "တိုက်ရထား", "နှစ်ရက်", "အရှည်", "အမြန်"
    );
    
    /**
     * Generate mock train routes between any two Myanmar stations
     */
    public List<Train> generateMockRoutes(Station source, Station destination, LocalDateTime journeyDate) {
        List<Train> mockTrains = new ArrayList<>();
        
        // Calculate approximate distance
        int distance = fareCalculatorService.getApproximateDistance(source.getCity(), destination.getCity());
        
        // Generate 3-5 mock trains for this route
        Random random = new Random();
        int numberOfTrains = 3 + random.nextInt(3); // 3-5 trains
        
        for (int i = 1; i <= numberOfTrains; i++) {
            Train mockTrain = createMockTrain(source, destination, journeyDate, distance, i);
            mockTrains.add(mockTrain);
        }
        
        return mockTrains;
    }
    
    /**
     * Generate mock routes between all major stations
     */
    public void generateAllMockRoutes() {
        List<Station> stations = stationRepository.findAll();
        List<Train> existingTrains = trainRepository.findAll();
        
        // Only generate if we don't have enough trains
        if (existingTrains.size() < 10) {
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            
            // Generate routes between major stations
            List<Station> majorStations = stations.stream()
                .filter(s -> s.getCode().matches("YGN|MDY|NPT|BGN|MAW|MYK"))
                .toList();
            
            for (int i = 0; i < majorStations.size(); i++) {
                for (int j = 0; j < majorStations.size(); j++) {
                    if (i != j) {
                        Station source = majorStations.get(i);
                        Station destination = majorStations.get(j);
                        
                        List<Train> mockTrains = generateMockRoutes(source, destination, tomorrow);
                        trainRepository.saveAll(mockTrains);
                    }
                }
            }
        }
    }
    
    private Train createMockTrain(Station source, Station destination, LocalDateTime journeyDate, 
                                 int distanceKm, int trainIndex) {
        Random random = new Random();
        
        Train train = new Train();
        
        // Generate train number (MMR = Myanmar Railways)
        String trainNumber = String.format("MMR-%03d", trainIndex + 100);
        
        // Generate Myanmar train name
        String trainName = generateMyanmarTrainName(source, destination);
        
        // Calculate travel time based on distance
        int travelHours = calculateTravelTime(distanceKm);
        
        // Set departure time (7 AM, 10 AM, 2 PM, 6 PM, 9 PM)
        int departureHour = getDepartureHour(trainIndex);
        LocalDateTime departureTime = journeyDate
            .withHour(departureHour)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);
        
        // Set arrival time
        LocalDateTime arrivalTime = departureTime.plusHours(travelHours);
        
        // Generate seats
        int totalSeats = getSeatCapacity(trainIndex);
        int availableSeats = random.nextInt(totalSeats - 10) + 10; // At least 10 seats available
        
        // Get train type
        String trainType = getTrainTypeBasedOnDistance(distanceKm);
        
        // Calculate base price
        double basePrice = fareCalculatorService.calculateFare(distanceKm, trainType);
        
        // Set train properties
        train.setTrainNumber(trainNumber);
        train.setTrainName(trainName);
        train.setSourceStation(source);
        train.setDestinationStation(destination);
        train.setDepartureTime(departureTime);
        train.setArrivalTime(arrivalTime);
        train.setTotalSeats(totalSeats);
        train.setAvailableSeats(availableSeats);
        train.setBasePrice(basePrice);
        train.setTrainType(trainType);
        train.setStatus("ACTIVE");
        
        return train;
    }
    
    private String generateMyanmarTrainName(Station source, Station destination) {
        Random random = new Random();
        String prefix = TRAIN_NAME_PREFIXES.get(random.nextInt(TRAIN_NAME_PREFIXES.size()));
        String suffix = TRAIN_NAME_SUFFIXES.get(random.nextInt(TRAIN_NAME_SUFFIXES.size()));
        
        return String.format("%s %s-%s %s", 
            prefix, 
            source.getCity(), 
            destination.getCity(), 
            suffix);
    }
    
    private int calculateTravelTime(int distanceKm) {
        // Travel speed: 50-60 km/h for express, 30-40 km/h for local
        Random random = new Random();
        int baseSpeed = 40; // Average speed in km/h
        int speedVariation = random.nextInt(21) - 10; // -10 to +10 km/h variation
        
        int speed = baseSpeed + speedVariation;
        int travelHours = (int) Math.ceil((double) distanceKm / speed);
        
        // Minimum 1 hour, maximum 24 hours
        return Math.max(1, Math.min(24, travelHours));
    }
    
    private int getDepartureHour(int trainIndex) {
        // Common departure times: 7, 10, 14, 18, 21
        int[] departureHours = {7, 10, 14, 18, 21};
        return departureHours[trainIndex % departureHours.length];
    }
    
    private int getSeatCapacity(int trainIndex) {
        // Different seat capacities
        int[] capacities = {150, 180, 200, 220, 250, 300};
        return capacities[trainIndex % capacities.length];
    }
    
    private String getTrainTypeBasedOnDistance(int distanceKm) {
        if (distanceKm > 500) {
            return "Express";
        } else if (distanceKm > 200) {
            return "Special";
        } else if (distanceKm > 100) {
            return "Local";
        } else {
            return "Day";
        }
    }
    
    /**
     * Get popular routes (top 5 based on station importance)
     */
    public List<Map<String, Object>> getPopularRoutes() {
        List<Map<String, Object>> popularRoutes = new ArrayList<>();
        
        // Define popular Myanmar routes
        String[][] routePairs = {
            {"Yangon", "Mandalay"},
            {"Yangon", "Naypyitaw"},
            {"Yangon", "Mawlamyine"},
            {"Mandalay", "Bago"},
            {"Mandalay", "Myitkyina"},
            {"Naypyitaw", "Mandalay"},
            {"Bago", "Pyay"},
            {"Yangon", "Pyin Oo Lwin"}
        };
        
        for (String[] route : routePairs) {
            Map<String, Object> routeInfo = new HashMap<>();
            routeInfo.put("sourceCity", route[0]);
            routeInfo.put("destinationCity", route[1]);
            
            // Get stations for these cities
            List<Station> sourceStations = stationRepository.findByCity(route[0]);
            List<Station> destStations = stationRepository.findByCity(route[1]);
            
            if (!sourceStations.isEmpty() && !destStations.isEmpty()) {
                routeInfo.put("sourceStation", sourceStations.get(0));
                routeInfo.put("destinationStation", destStations.get(0));
                
                // Calculate distance
                int distance = fareCalculatorService.getApproximateDistance(route[0], route[1]);
                routeInfo.put("distanceKm", distance);
                
                // Estimated travel time
                routeInfo.put("estimatedTravelTime", calculateTravelTime(distance) + " hours");
                
                // Frequency
                routeInfo.put("frequency", getRouteFrequency(route[0], route[1]));
                
                popularRoutes.add(routeInfo);
            }
        }
        
        return popularRoutes;
    }
    
    private String getRouteFrequency(String sourceCity, String destinationCity) {
        // Determine frequency based on route importance
        Map<String, String> frequencyMap = new HashMap<>();
        frequencyMap.put("Yangon-Mandalay", "Daily (Multiple trains)");
        frequencyMap.put("Yangon-Naypyitaw", "Daily");
        frequencyMap.put("Yangon-Mawlamyine", "Daily");
        frequencyMap.put("Mandalay-Bago", "Every 2 days");
        frequencyMap.put("Mandalay-Myitkyina", "3 times per week");
        frequencyMap.put("Naypyitaw-Mandalay", "Daily");
        frequencyMap.put("Bago-Pyay", "Every 3 days");
        frequencyMap.put("Yangon-Pyin Oo Lwin", "Weekly");
        
        String key = sourceCity + "-" + destinationCity;
        return frequencyMap.getOrDefault(key, "Check schedule");
    }
    
    /**
     * Search for routes between any two cities
     */
    public List<Map<String, Object>> searchRoutes(String sourceCity, String destinationCity, 
                                                  LocalDateTime journeyDate, int numberOfPassengers) {
        List<Map<String, Object>> searchResults = new ArrayList<>();
        
        // Find stations for the cities
        List<Station> sourceStations = stationRepository.findByCity(sourceCity);
        List<Station> destStations = stationRepository.findByCity(destinationCity);
        
        if (sourceStations.isEmpty() || destStations.isEmpty()) {
            return searchResults; // Empty if no stations found
        }
        
        Station sourceStation = sourceStations.get(0);
        Station destStation = destStations.get(0);
        
        // Get existing trains for this route
        LocalDateTime startOfDay = journeyDate.toLocalDate().atStartOfDay();
        LocalDateTime nextDay = startOfDay.plusDays(1);
        List<Train> existingTrains = trainRepository.findTrainsBetweenCitiesOnDate(
            sourceCity, destinationCity, startOfDay, nextDay
        );
        
        // If no existing trains, generate mock ones
        if (existingTrains.isEmpty()) {
            existingTrains = generateMockRoutes(sourceStation, destStation, journeyDate);
        }
        
        // Format results
        for (Train train : existingTrains) {
            Map<String, Object> result = new HashMap<>();
            result.put("train", train);
            result.put("sourceStation", train.getSourceStation());
            result.put("destinationStation", train.getDestinationStation());
            
            // Calculate fare for this route
            double farePerPassenger = fareCalculatorService.calculateFare(
                fareCalculatorService.getApproximateDistance(sourceCity, destinationCity),
                train.getTrainType()
            );
            
            result.put("farePerPassenger", farePerPassenger);
            result.put("totalFare", farePerPassenger * numberOfPassengers);
            result.put("hasEnoughSeats", train.getAvailableSeats() >= numberOfPassengers);
            result.put("availableSeats", train.getAvailableSeats());
            
            // Travel duration
            long hours = java.time.Duration.between(train.getDepartureTime(), train.getArrivalTime()).toHours();
            result.put("travelDuration", hours + " hours");
            
            searchResults.add(result);
        }
        
        return searchResults;
    }
}