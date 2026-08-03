package com.ticket.booking.service;

import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MockRouteService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private FareCalculatorService fareCalculatorService;

    // -----------------------------------------------------------------------
    // Authentic Myanmar Railways named trains (MMR UP/DN train number system)
    // Each entry: { trainNumber, trainName, trainType, departureHour, speedKmh }
    // -----------------------------------------------------------------------
    private static final List<String[]> NAMED_TRAINS = Arrays.asList(
        new String[]{"UP-1",  "Dagon Express",          "Express",  "6",  "55"},
        new String[]{"DN-2",  "Dagon Express",          "Express",  "18", "55"},
        new String[]{"UP-3",  "Mandalar Minn",          "Express",  "15", "50"},
        new String[]{"DN-4",  "Mandalar Minn",          "Express",  "7",  "50"},
        new String[]{"UP-5",  "Shwedagon Express",      "Special",  "8",  "60"},
        new String[]{"DN-6",  "Shwedagon Express",      "Special",  "8",  "60"},
        new String[]{"UP-11", "Bagan Express",          "Express",  "6",  "50"},
        new String[]{"DN-12", "Bagan Express",          "Express",  "18", "50"},
        new String[]{"UP-13", "Ayeyarwady Special",     "Special",  "7",  "45"},
        new String[]{"DN-14", "Ayeyarwady Special",     "Special",  "19", "45"},
        new String[]{"UP-15", "Inle Express",           "Express",  "9",  "50"},
        new String[]{"DN-16", "Inle Express",           "Express",  "9",  "50"},
        new String[]{"UP-31", "Myitkyina Express",      "Express",  "5",  "45"},
        new String[]{"DN-32", "Myitkyina Express",      "Express",  "16", "45"},
        new String[]{"UP-35", "Lashio Mail",            "Mail",     "6",  "40"},
        new String[]{"DN-36", "Lashio Mail",            "Mail",     "18", "40"},
        new String[]{"UP-37", "Shan State Special",     "Special",  "9",  "40"},
        new String[]{"DN-38", "Shan State Special",     "Special",  "15", "40"},
        new String[]{"UP-41", "Mawlamyine Express",     "Express",  "7",  "45"},
        new String[]{"DN-42", "Mawlamyine Express",     "Express",  "7",  "45"},
        new String[]{"UP-51", "Kalaw Scenic",           "Scenic",   "8",  "35"},
        new String[]{"DN-52", "Kalaw Scenic",           "Scenic",   "14", "35"},
        new String[]{"UP-55", "Naypyitaw Special",      "Special",  "6",  "55"},
        new String[]{"DN-56", "Naypyitaw Special",      "Special",  "13", "55"},
        new String[]{"UP-61", "Pyay Ordinary",          "Ordinary", "6",  "35"},
        new String[]{"DN-62", "Pyay Ordinary",          "Ordinary", "13", "35"},
        new String[]{"UP-71", "Taungoo Local",          "Local",    "7",  "38"},
        new String[]{"DN-72", "Taungoo Local",          "Local",    "14", "38"},
        new String[]{"UP-81", "Bago Suburban",          "Suburban", "6",  "40"},
        new String[]{"DN-82", "Bago Suburban",          "Suburban", "17", "40"},
        new String[]{"UP-83", "Circular Line",          "Circular", "5",  "30"},
        new String[]{"DN-84", "Circular Line",          "Circular", "12", "30"},
        new String[]{"UP-91", "Pathein DEMU",           "DEMU",     "7",  "65"},
        new String[]{"DN-92", "Pathein DEMU",           "DEMU",     "14", "65"},
        new String[]{"UP-93", "Yangon-Bago DEMU",       "DEMU",     "6",  "65"},
        new String[]{"DN-94", "Yangon-Bago DEMU",       "DEMU",     "18", "65"},
        new String[]{"UP-95", "Meiktila DEMU",          "DEMU",     "8",  "60"},
        new String[]{"DN-96", "Meiktila DEMU",          "DEMU",     "16", "60"},
        new String[]{"UP-97", "Taungoo DEMU",           "DEMU",     "7",  "62"},
        new String[]{"DN-98", "Taungoo DEMU",           "DEMU",     "15", "62"},
        new String[]{"UP-101","Monywa Mixed",           "Mixed",    "9",  "30"},
        new String[]{"DN-102","Monywa Mixed",           "Mixed",    "15", "30"},
        new String[]{"UP-111","Night Express",          "Night",    "21", "55"},
        new String[]{"DN-112","Night Express",          "Night",    "20", "55"},
        new String[]{"UP-113","Kalay Ordinary",         "Ordinary", "7",  "35"},
        new String[]{"DN-114","Kalay Ordinary",         "Ordinary", "7",  "35"},
        new String[]{"UP-121","Pyin Oo Lwin Special",   "Special",  "10", "45"},
        new String[]{"DN-122","Pyin Oo Lwin Special",   "Special",  "14", "45"},
        new String[]{"UP-131","Sagaing Local",          "Local",    "8",  "40"},
        new String[]{"DN-132","Sagaing Local",          "Local",    "15", "40"},
        new String[]{"UP-141","Shwebo Mail",            "Mail",     "7",  "40"},
        new String[]{"DN-142","Shwebo Mail",            "Mail",     "14", "40"}
    );

    // -----------------------------------------------------------------------
    // Popular routes covering the full Myanmar rail network
    // -----------------------------------------------------------------------
    private static final String[][] POPULAR_ROUTE_PAIRS = {
        {"Yangon",    "Mandalay"},
        {"Yangon",    "Naypyitaw"},
        {"Yangon",    "Mawlamyine"},
        {"Yangon",    "Bago"},
        {"Yangon",    "Pyay"},
        {"Yangon",    "Taungoo"},
        {"Yangon",    "Pyin Oo Lwin"},
        {"Yangon",    "Myitkyina"},
        {"Mandalay",  "Naypyitaw"},
        {"Mandalay",  "Pyin Oo Lwin"},
        {"Mandalay",  "Lashio"},
        {"Mandalay",  "Myitkyina"},
        {"Mandalay",  "Bagan"},
        {"Mandalay",  "Kalay"},
        {"Mandalay",  "Taunggyi"},
        {"Naypyitaw", "Bago"},
        {"Thazi",     "Kalaw"},
        {"Thazi",     "Taunggyi"},
        {"Bago",      "Mawlamyine"},
        {"Mandalay",  "Shwebo"}
    };

    private static final int TRAINS_PER_ROUTE = 8;

    // -----------------------------------------------------------------------
    // Public API methods
    // -----------------------------------------------------------------------

    /**
     * Generate mock train routes between any two Myanmar stations.
     */
    public List<Train> generateMockRoutes(Station source, Station destination, LocalDateTime journeyDate) {
        int distance = fareCalculatorService.getApproximateDistance(source.getCity(), destination.getCity());
        List<String[]> candidates = selectTrainCandidates(source.getCity(), destination.getCity(), TRAINS_PER_ROUTE);
        List<Train> mockTrains = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i++) {
            mockTrains.add(createTrainFromTemplate(source, destination, journeyDate, distance, candidates.get(i), i));
        }
        return mockTrains;
    }

    /**
     * Generate mock routes between all major stations (admin bootstrap).
     */
    public void generateAllMockRoutes() {
        List<Station> stations = stationRepository.findAll();
        List<Train> existingTrains = trainRepository.findAll();

        if (existingTrains.size() < 10) {
            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            List<Station> majorStations = stations.stream()
                .filter(s -> s.getCode().matches("YGN|MDY|NPT|NPY|BGN|MAW|MYK|LAS|TGI|KLW"))
                .toList();
            for (int i = 0; i < majorStations.size(); i++) {
                for (int j = 0; j < majorStations.size(); j++) {
                    if (i != j) {
                        List<Train> mockTrains = generateMockRoutes(majorStations.get(i), majorStations.get(j), tomorrow);
                        trainRepository.saveAll(mockTrains);
                    }
                }
            }
        }
    }

    /**
     * Get popular routes (list only, no train objects).
     */
    public List<Map<String, Object>> getPopularRoutes() {
        List<Map<String, Object>> popularRoutes = new ArrayList<>();
        for (String[] route : POPULAR_ROUTE_PAIRS) {
            List<Station> sourceStations = stationRepository.findByCity(route[0]);
            List<Station> destStations   = stationRepository.findByCity(route[1]);
            if (!sourceStations.isEmpty() && !destStations.isEmpty()) {
                Map<String, Object> routeInfo = new HashMap<>();
                routeInfo.put("sourceCity",         route[0]);
                routeInfo.put("destinationCity",    route[1]);
                routeInfo.put("sourceStation",      sourceStations.get(0));
                routeInfo.put("destinationStation", destStations.get(0));
                int distance = fareCalculatorService.getApproximateDistance(route[0], route[1]);
                routeInfo.put("distanceKm",           distance);
                routeInfo.put("estimatedTravelTime",  estimateTravelTime(distance) + " hours");
                routeInfo.put("frequency",            getRouteFrequency(route[0], route[1]));
                popularRoutes.add(routeInfo);
            }
        }
        return popularRoutes;
    }

    /**
     * Get popular routes with trains for each.
     */
    public List<Map<String, Object>> getPopularRoutesWithTrains() {
        List<Map<String, Object>> popularRoutes = new ArrayList<>();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        for (String[] route : POPULAR_ROUTE_PAIRS) {
            List<Station> sourceStations = stationRepository.findByCity(route[0]);
            List<Station> destStations   = stationRepository.findByCity(route[1]);
            if (!sourceStations.isEmpty() && !destStations.isEmpty()) {
                Station sourceStation = sourceStations.get(0);
                Station destStation   = destStations.get(0);
                Map<String, Object> routeInfo = new HashMap<>();
                routeInfo.put("sourceCity",   route[0]);
                routeInfo.put("destinationCity", route[1]);
                routeInfo.put("routeName",    route[0] + " - " + route[1]);
                routeInfo.put("routeCode",
                    "MMR-" + route[0].substring(0, Math.min(3, route[0].length())).toUpperCase()
                    + "-" + route[1].substring(0, Math.min(3, route[1].length())).toUpperCase());
                routeInfo.put("sourceStation",      sourceStation);
                routeInfo.put("destinationStation", destStation);
                int distance = fareCalculatorService.getApproximateDistance(route[0], route[1]);
                routeInfo.put("distanceKm",           distance);
                routeInfo.put("estimatedTravelTime",  estimateTravelTime(distance) + " hours");
                routeInfo.put("frequency",            getRouteFrequency(route[0], route[1]));
                List<Train> trains = generateMockRoutes(sourceStation, destStation, tomorrow);
                routeInfo.put("trains", trains);
                double totalFare = trains.stream().mapToDouble(Train::getBasePrice).sum();
                double averageFare = trains.isEmpty() ? 0 : totalFare / trains.size();
                routeInfo.put("averageFare", averageFare);
                routeInfo.put("baseFare",    averageFare);
                popularRoutes.add(routeInfo);
            }
        }
        return popularRoutes;
    }

    /**
     * Search for routes between any two cities.
     */
    public List<Map<String, Object>> searchRoutes(String sourceCity, String destinationCity,
                                                   LocalDateTime journeyDate, int numberOfPassengers) {
        List<Map<String, Object>> searchResults = new ArrayList<>();

        List<Station> sourceStations = stationRepository.findByCity(sourceCity);
        List<Station> destStations   = stationRepository.findByCity(destinationCity);
        if (sourceStations.isEmpty() || destStations.isEmpty()) return searchResults;

        Station sourceStation = sourceStations.get(0);
        Station destStation   = destStations.get(0);

        LocalDateTime startOfDay = journeyDate.toLocalDate().atStartOfDay();
        LocalDateTime nextDay    = startOfDay.plusDays(1);
        List<Train> existingTrains = trainRepository.findTrainsBetweenCitiesOnDate(
            sourceCity, destinationCity, startOfDay, nextDay);

        List<Train> allTrains = new ArrayList<>(existingTrains);
        int needed = TRAINS_PER_ROUTE - existingTrains.size();
        if (needed > 0) {
            List<Train> generated = generateMockRoutes(sourceStation, destStation, journeyDate);
            for (int i = 0; i < Math.min(needed, generated.size()); i++) {
                allTrains.add(generated.get(i));
            }
        }
        while (allTrains.size() < TRAINS_PER_ROUTE) {
            List<Train> extra = generateMockRoutes(sourceStation, destStation, journeyDate);
            if (extra.isEmpty()) break;
            allTrains.add(extra.get(0));
        }

        int distance = fareCalculatorService.getApproximateDistance(sourceCity, destinationCity);
        for (Train train : allTrains) {
            Map<String, Object> result = new HashMap<>();
            result.put("train",              train);
            result.put("sourceStation",      train.getSourceStation());
            result.put("destinationStation", train.getDestinationStation());
            double farePerPassenger = fareCalculatorService.calculateFare(distance, train.getTrainType());
            result.put("farePerPassenger",  farePerPassenger);
            result.put("totalFare",         farePerPassenger * numberOfPassengers);
            result.put("hasEnoughSeats",    train.getAvailableSeats() >= numberOfPassengers);
            result.put("availableSeats",    train.getAvailableSeats());
            long hours = java.time.Duration.between(train.getDepartureTime(), train.getArrivalTime()).toHours();
            long mins  = java.time.Duration.between(train.getDepartureTime(), train.getArrivalTime()).toMinutesPart();
            result.put("travelDuration", hours + " hours" + (mins > 0 ? " " + mins + " mins" : ""));
            searchResults.add(result);
        }
        return searchResults;
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private List<String[]> selectTrainCandidates(String sourceCity, String destCity, int count) {
        int distance = fareCalculatorService.getApproximateDistance(sourceCity, destCity);
        String preferredType = getPreferredTypeForDistance(distance);

        List<String[]> allTemplates = new ArrayList<>(NAMED_TRAINS);
        long seed = (long) sourceCity.hashCode() * 31 + destCity.hashCode();
        Collections.shuffle(allTemplates, new Random(seed));

        // Sort so preferred type comes first
        allTemplates.sort((a, b) -> {
            boolean aMatch = a[2].equalsIgnoreCase(preferredType);
            boolean bMatch = b[2].equalsIgnoreCase(preferredType);
            if (aMatch == bMatch) return 0;
            return aMatch ? -1 : 1;
        });

        // Get type-diverse set first, then fill from rest
        Set<String> seenTypes = new LinkedHashSet<>();
        List<String[]> diverse = new ArrayList<>();
        List<String[]> rest    = new ArrayList<>();
        for (String[] t : allTemplates) {
            if (!seenTypes.contains(t[2]) && diverse.size() < 6) {
                seenTypes.add(t[2]);
                diverse.add(t);
            } else {
                rest.add(t);
            }
        }
        List<String[]> pool = new ArrayList<>();
        pool.addAll(diverse);
        pool.addAll(rest);

        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(pool.get(i % pool.size()));
        }
        return result;
    }

    private String getPreferredTypeForDistance(int distanceKm) {
        if (distanceKm > 500)      return "Express";
        else if (distanceKm > 300) return "Special";
        else if (distanceKm > 150) return "DEMU";
        else if (distanceKm > 80)  return "Ordinary";
        else                       return "Suburban";
    }

    private Train createTrainFromTemplate(Station source, Station destination, LocalDateTime journeyDate,
                                          int distanceKm, String[] template, int index) {
        String baseTrainNumber = template[0];
        String trainName       = template[1];
        String trainType       = template[2];
        int departureHour      = Integer.parseInt(template[3]);
        int speedKmh           = Integer.parseInt(template[4]);

        // Unique train number per route
        String src3 = source.getCity().substring(0, Math.min(3, source.getCity().length())).toUpperCase();
        String dst3 = destination.getCity().substring(0, Math.min(3, destination.getCity().length())).toUpperCase();
        String trainNumber = baseTrainNumber + "-" + src3 + dst3 + (index > 0 ? "-" + index : "");

        // Travel time: distance / speed + stop time
        int travelMinutes = (int) Math.round((distanceKm / (double) speedKmh) * 60);
        int stopMinutes   = (distanceKm / 50) * 7;
        int totalMinutes  = Math.max(60, Math.min(travelMinutes + stopMinutes, 24 * 60));

        int adjustedHour = (departureHour + index) % 24;
        LocalDateTime departureTime = journeyDate.withHour(adjustedHour).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime arrivalTime   = departureTime.plusMinutes(totalMinutes);

        int totalSeats     = getSeatCapacityByType(trainType);
        int availableSeats = new Random().nextInt(Math.max(1, totalSeats - 5)) + 5;

        double basePrice = fareCalculatorService.calculateFare(distanceKm, trainType);

        Train train = new Train();
        train.setTrainNumber(trainNumber);
        train.setTrainName(trainName + " (" + source.getCity() + " - " + destination.getCity() + ")");
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

    private int getSeatCapacityByType(String trainType) {
        switch (trainType.toUpperCase()) {
            case "EXPRESS":  return 350;
            case "SPECIAL":  return 300;
            case "NIGHT":    return 280;
            case "SCENIC":   return 120;
            case "DEMU":     return 200;
            case "ORDINARY": return 250;
            case "MAIL":     return 180;
            case "MIXED":    return 150;
            case "CIRCULAR": return 180;
            case "SUBURBAN": return 220;
            default:         return 200;
        }
    }

    private int estimateTravelTime(int distanceKm) {
        int minutes = (int) Math.round((distanceKm / 45.0) * 60);
        return Math.max(1, minutes / 60);
    }

    private String getRouteFrequency(String sourceCity, String destinationCity) {
        Map<String, String> frequencyMap = new HashMap<>();
        frequencyMap.put("Yangon-Mandalay",      "Daily (6 trains)");
        frequencyMap.put("Yangon-Naypyitaw",      "Daily (4 trains)");
        frequencyMap.put("Yangon-Mawlamyine",     "Daily (2 trains)");
        frequencyMap.put("Yangon-Bago",           "Daily (Multiple trains)");
        frequencyMap.put("Yangon-Pyay",           "Daily");
        frequencyMap.put("Yangon-Taungoo",        "Daily");
        frequencyMap.put("Yangon-Pyin Oo Lwin",   "3 times per week");
        frequencyMap.put("Yangon-Myitkyina",      "3 times per week");
        frequencyMap.put("Mandalay-Naypyitaw",    "Daily (3 trains)");
        frequencyMap.put("Mandalay-Pyin Oo Lwin", "Daily");
        frequencyMap.put("Mandalay-Lashio",       "Daily");
        frequencyMap.put("Mandalay-Myitkyina",    "3 times per week");
        frequencyMap.put("Mandalay-Bagan",        "Daily");
        frequencyMap.put("Mandalay-Kalay",        "Every 2 days");
        frequencyMap.put("Mandalay-Taunggyi",     "Daily");
        frequencyMap.put("Naypyitaw-Bago",        "Daily");
        frequencyMap.put("Thazi-Kalaw",           "Daily");
        frequencyMap.put("Thazi-Taunggyi",        "Daily");
        frequencyMap.put("Bago-Mawlamyine",       "Daily");
        frequencyMap.put("Mandalay-Shwebo",       "Daily");
        String key = sourceCity + "-" + destinationCity;
        String rev = destinationCity + "-" + sourceCity;
        return frequencyMap.getOrDefault(key, frequencyMap.getOrDefault(rev, "Check schedule"));
    }
}