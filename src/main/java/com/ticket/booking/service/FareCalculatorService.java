package com.ticket.booking.service;

import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FareCalculatorService {
    
    // Distance matrix between major stations in km (approximate)
    private static final Map<String, Map<String, Integer>> DISTANCE_MATRIX = new HashMap<>();
    
    static {
        // Initialize distance matrix with Myanmar Railways approximate distances
        Map<String, Integer> yangonDistances = new HashMap<>();
        yangonDistances.put("Bago", 80);
        yangonDistances.put("Pyay", 260);
        yangonDistances.put("Taungoo", 250);
        yangonDistances.put("Naypyitaw", 320);
        yangonDistances.put("Naypyidaw", 320);
        yangonDistances.put("Thazi", 450);
        yangonDistances.put("Mandalay", 620);
        yangonDistances.put("Mawlamyine", 300);
        yangonDistances.put("Mawlamyaing", 300);
        yangonDistances.put("Pyin Oo Lwin", 700);
        yangonDistances.put("Pyinoolwin", 700);
        yangonDistances.put("Myitkyina", 1450);
        yangonDistances.put("Bagan", 700);
        yangonDistances.put("Kalay", 1050);
        yangonDistances.put("Lashio", 950);
        yangonDistances.put("Taunggyi", 600);
        yangonDistances.put("Kalaw", 580);
        yangonDistances.put("Pathein", 190);
        yangonDistances.put("Dawei", 620);
        yangonDistances.put("Meiktila", 430);
        DISTANCE_MATRIX.put("Yangon", yangonDistances);

        Map<String, Integer> mandalayDistances = new HashMap<>();
        mandalayDistances.put("Naypyitaw", 300);
        mandalayDistances.put("Naypyidaw", 300);
        mandalayDistances.put("Pyin Oo Lwin", 70);
        mandalayDistances.put("Pyinoolwin", 70);
        mandalayDistances.put("Lashio", 300);
        mandalayDistances.put("Myitkyina", 750);
        mandalayDistances.put("Kalay", 420);
        mandalayDistances.put("Bagan", 190);
        mandalayDistances.put("Thazi", 170);
        mandalayDistances.put("Meiktila", 130);
        mandalayDistances.put("Taunggyi", 280);
        mandalayDistances.put("Kalaw", 260);
        mandalayDistances.put("Shwebo", 110);
        mandalayDistances.put("Sagaing", 20);
        mandalayDistances.put("Monywa", 136);
        DISTANCE_MATRIX.put("Mandalay", mandalayDistances);

        Map<String, Integer> naypyitawDistances = new HashMap<>();
        naypyitawDistances.put("Yangon", 320);
        naypyitawDistances.put("Mandalay", 300);
        naypyitawDistances.put("Bago", 240);
        naypyitawDistances.put("Taungoo", 100);
        naypyitawDistances.put("Thazi", 130);
        naypyitawDistances.put("Meiktila", 130);
        DISTANCE_MATRIX.put("Naypyitaw", naypyitawDistances);
        DISTANCE_MATRIX.put("Naypyidaw", naypyitawDistances);

        Map<String, Integer> bagoDistances = new HashMap<>();
        bagoDistances.put("Yangon", 80);
        bagoDistances.put("Naypyitaw", 240);
        bagoDistances.put("Pyay", 180);
        bagoDistances.put("Taungoo", 170);
        bagoDistances.put("Mawlamyine", 220);
        DISTANCE_MATRIX.put("Bago", bagoDistances);

        Map<String, Integer> thinkDistances = new HashMap<>();
        thinkDistances.put("Mandalay", 170);
        thinkDistances.put("Kalaw", 100);
        thinkDistances.put("Taunggyi", 140);
        thinkDistances.put("Meiktila", 40);
        thinkDistances.put("Naypyitaw", 130);
        DISTANCE_MATRIX.put("Thazi", thinkDistances);

        Map<String, Integer> pyinoolwinDistances = new HashMap<>();
        pyinoolwinDistances.put("Mandalay", 70);
        pyinoolwinDistances.put("Lashio", 230);
        pyinoolwinDistances.put("Yangon", 700);
        DISTANCE_MATRIX.put("Pyin Oo Lwin", pyinoolwinDistances);
        DISTANCE_MATRIX.put("Pyinoolwin", pyinoolwinDistances);

        Map<String, Integer> lashioDistances = new HashMap<>();
        lashioDistances.put("Mandalay", 300);
        lashioDistances.put("Pyin Oo Lwin", 230);
        lashioDistances.put("Pyinoolwin", 230);
        DISTANCE_MATRIX.put("Lashio", lashioDistances);

        Map<String, Integer> myitkyinaDistances = new HashMap<>();
        myitkyinaDistances.put("Mandalay", 750);
        myitkyinaDistances.put("Yangon", 1450);
        myitkyinaDistances.put("Shwebo", 640);
        DISTANCE_MATRIX.put("Myitkyina", myitkyinaDistances);

        Map<String, Integer> mawlamyineDistances = new HashMap<>();
        mawlamyineDistances.put("Yangon", 300);
        mawlamyineDistances.put("Bago", 220);
        mawlamyineDistances.put("Dawei", 320);
        DISTANCE_MATRIX.put("Mawlamyine", mawlamyineDistances);
        DISTANCE_MATRIX.put("Mawlamyaing", mawlamyineDistances);

        Map<String, Integer> kaywDistances = new HashMap<>();
        kaywDistances.put("Thazi", 100);
        kaywDistances.put("Mandalay", 260);
        kaywDistances.put("Taunggyi", 50);
        DISTANCE_MATRIX.put("Kalaw", kaywDistances);

        Map<String, Integer> taunggyiDistances = new HashMap<>();
        taunggyiDistances.put("Thazi", 140);
        taunggyiDistances.put("Mandalay", 280);
        taunggyiDistances.put("Kalaw", 50);
        taunggyiDistances.put("Yangon", 600);
        DISTANCE_MATRIX.put("Taunggyi", taunggyiDistances);

        Map<String, Integer> baganDistances = new HashMap<>();
        baganDistances.put("Mandalay", 190);
        baganDistances.put("Yangon", 700);
        baganDistances.put("Pyay", 310);
        DISTANCE_MATRIX.put("Bagan", baganDistances);

        Map<String, Integer> kalayDistances = new HashMap<>();
        kalayDistances.put("Mandalay", 420);
        kalayDistances.put("Shwebo", 310);
        DISTANCE_MATRIX.put("Kalay", kalayDistances);
    }
    
    /**
     * Calculate approximate distance between two cities in Myanmar
     */
    public int getApproximateDistance(String sourceCity, String destinationCity) {
        if (sourceCity.equals(destinationCity)) {
            return 0;
        }
        
        // Check direct distance
        if (DISTANCE_MATRIX.containsKey(sourceCity)) {
            Map<String, Integer> distances = DISTANCE_MATRIX.get(sourceCity);
            if (distances.containsKey(destinationCity)) {
                return distances.get(destinationCity);
            }
        }
        
        // Check reverse distance
        if (DISTANCE_MATRIX.containsKey(destinationCity)) {
            Map<String, Integer> distances = DISTANCE_MATRIX.get(destinationCity);
            if (distances.containsKey(sourceCity)) {
                return distances.get(sourceCity);
            }
        }
        
        // Estimate based on known routes
        return estimateDistance(sourceCity, destinationCity);
    }
    
    private int estimateDistance(String sourceCity, String destinationCity) {
        // Approximate distances from Yangon for estimation
        Map<String, Integer> centralDistances = new HashMap<>();
        centralDistances.put("Yangon", 0);
        centralDistances.put("Mandalay", 620);
        centralDistances.put("Naypyitaw", 320);
        centralDistances.put("Naypyidaw", 320);
        centralDistances.put("Bago", 80);
        centralDistances.put("Pyay", 260);
        centralDistances.put("Taungoo", 250);
        centralDistances.put("Mawlamyine", 300);
        centralDistances.put("Mawlamyaing", 300);
        centralDistances.put("Pyin Oo Lwin", 700);
        centralDistances.put("Pyinoolwin", 700);
        centralDistances.put("Myitkyina", 1450);
        centralDistances.put("Lashio", 950);
        centralDistances.put("Kalay", 1050);
        centralDistances.put("Thazi", 450);
        centralDistances.put("Meiktila", 430);
        centralDistances.put("Taunggyi", 600);
        centralDistances.put("Kalaw", 580);
        centralDistances.put("Bagan", 700);
        centralDistances.put("Shwebo", 730);
        centralDistances.put("Sagaing", 640);
        centralDistances.put("Monywa", 756);
        centralDistances.put("Pathein", 190);
        centralDistances.put("Dawei", 620);

        int sourceDist = centralDistances.getOrDefault(sourceCity, 300);
        int destDist = centralDistances.getOrDefault(destinationCity, 300);

        return Math.max(50, Math.abs(sourceDist - destDist));
    }
    
    /**
     * Calculate Myanmar Railways fare based on distance and train type
     */
    public double calculateFare(int distanceKm, String trainType) {
        double baseFare;
        
        // Myanmar Railways fare structure (in MMK)
        if (distanceKm <= 100) {
            baseFare = 2000; // Base fare for short distance
        } else if (distanceKm <= 300) {
            baseFare = 5000;
        } else if (distanceKm <= 600) {
            baseFare = 10000;
        } else {
            baseFare = 15000;
        }
        
        // Apply train type multiplier
        double multiplier = getTrainTypeMultiplier(trainType);
        
        // Add distance-based fare
        double distanceFare = distanceKm * getPerKmRate(trainType);
        
        return baseFare * multiplier + distanceFare;
    }
    
    private double getTrainTypeMultiplier(String trainType) {
        switch (trainType.toUpperCase()) {
            case "EXPRESS":
                return 1.5;
            case "SPECIAL":
                return 1.8;
            case "SCENIC":
                return 2.2;
            case "LOCAL":
                return 1.0;
            case "DEMU":            // Diesel Electric Multiple Unit
                return 1.3;
            case "ORDINARY":
                return 0.9;
            case "MAIL":
                return 1.1;
            case "MIXED":
                return 0.85;
            case "CIRCULAR":
                return 1.0;
            case "SUBURBAN":
                return 1.0;
            case "NIGHT":
                return 1.6;
            default:
                return 1.2; // Default for other types
        }
    }

    private double getPerKmRate(String trainType) {
        switch (trainType.toUpperCase()) {
            case "EXPRESS":
                return 25.0; // MMK per km
            case "SPECIAL":
                return 30.0;
            case "SCENIC":
                return 35.0;
            case "LOCAL":
                return 20.0;
            case "DEMU":
                return 22.0;
            case "ORDINARY":
                return 15.0;
            case "MAIL":
                return 18.0;
            case "MIXED":
                return 12.0;
            case "CIRCULAR":
                return 18.0;
            case "SUBURBAN":
                return 15.0;
            case "NIGHT":
                return 28.0;
            default:
                return 22.0;
        }
    }
    
    /**
     * Calculate discount based on passenger type
     */
    public double calculateDiscount(String passengerType, double fare) {
        switch (passengerType.toUpperCase()) {
            case "CHILD": // 5-12 years
                return fare * 0.50; // 50% discount
            case "STUDENT":
                return fare * 0.15; // 15% discount
            case "SENIOR": // 60+ years
                return fare * 0.20; // 20% discount
            case "GOVERNMENT":
                return fare * 0.10; // 10% discount
            default:
                return 0.0; // No discount for adults
        }
    }
    
    /**
     * Calculate Myanmar Railways service charges
     */
    public double calculateServiceCharges(double fare) {
        // Myanmar Railways charges 5% service fee
        double serviceCharge = fare * 0.05;
        
        // Minimum service charge
        return Math.max(serviceCharge, 500.0); // Minimum 500 MMK
    }
    
    /**
     * Calculate total fare for a booking
     */
    public double calculateTotalFare(Train train, int numberOfPassengers, 
                                     String passengerType, boolean includeServiceCharge) {
        int distance = getApproximateDistance(
            train.getSourceStation().getCity(),
            train.getDestinationStation().getCity()
        );
        
        double baseFare = calculateFare(distance, train.getTrainType());
        double discount = calculateDiscount(passengerType, baseFare);
        double discountedFare = baseFare - discount;
        
        double total = discountedFare * numberOfPassengers;
        
        if (includeServiceCharge) {
            total += calculateServiceCharges(total);
        }
        
        return Math.round(total * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    /**
     * Generate Myanmar Railways fare breakdown
     */
    public Map<String, Object> getFareBreakdown(Train train, int numberOfPassengers, 
                                                String passengerType) {
        Map<String, Object> breakdown = new HashMap<>();
        
        String sourceCity = train.getSourceStation().getCity();
        String destCity = train.getDestinationStation().getCity();
        int distance = getApproximateDistance(sourceCity, destCity);
        
        double baseFare = calculateFare(distance, train.getTrainType());
        double discount = calculateDiscount(passengerType, baseFare);
        double serviceCharge = calculateServiceCharges(baseFare - discount);
        
        breakdown.put("sourceCity", sourceCity);
        breakdown.put("destinationCity", destCity);
        breakdown.put("distanceKm", distance);
        breakdown.put("trainType", train.getTrainType());
        breakdown.put("trainNumber", train.getTrainNumber());
        breakdown.put("baseFarePerPassenger", baseFare);
        breakdown.put("discountPerPassenger", discount);
        breakdown.put("discountedFarePerPassenger", baseFare - discount);
        breakdown.put("serviceChargePerPassenger", serviceCharge);
        breakdown.put("numberOfPassengers", numberOfPassengers);
        breakdown.put("totalBaseFare", baseFare * numberOfPassengers);
        breakdown.put("totalDiscount", discount * numberOfPassengers);
        breakdown.put("totalServiceCharge", serviceCharge * numberOfPassengers);
        breakdown.put("totalFare", ((baseFare - discount + serviceCharge) * numberOfPassengers));
        
        return breakdown;
    }
}