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
        yangonDistances.put("Pyay", 180);
        yangonDistances.put("Taungoo", 250);
        yangonDistances.put("Naypyitaw", 320);
        yangonDistances.put("Thazi", 450);
        yangonDistances.put("Mandalay", 620);
        yangonDistances.put("Mawlamyine", 300);
        yangonDistances.put("Pyin Oo Lwin", 700);
        yangonDistances.put("Myitkyina", 1450);
        DISTANCE_MATRIX.put("Yangon", yangonDistances);
        
        Map<String, Integer> mandalayDistances = new HashMap<>();
        mandalayDistances.put("Naypyitaw", 300);
        mandalayDistances.put("Pyin Oo Lwin", 70);
        mandalayDistances.put("Lashio", 300);
        mandalayDistances.put("Myitkyina", 750);
        mandalayDistances.put("Kalay", 420);
        DISTANCE_MATRIX.put("Mandalay", mandalayDistances);
        
        Map<String, Integer> naypyitawDistances = new HashMap<>();
        naypyitawDistances.put("Yangon", 320);
        naypyitawDistances.put("Mandalay", 300);
        naypyitawDistances.put("Bago", 240);
        DISTANCE_MATRIX.put("Naypyitaw", naypyitawDistances);
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
        // Simple estimation based on Myanmar geography
        String[] cities = {"Yangon", "Mandalay", "Naypyitaw", "Bago", "Pyay", "Taungoo", 
                          "Mawlamyine", "Pyin Oo Lwin", "Myitkyina", "Lashio", "Kalay"};
        
        // Approximate distances in Myanmar
        Map<String, Integer> centralDistances = new HashMap<>();
        centralDistances.put("Yangon", 0);
        centralDistances.put("Mandalay", 620);
        centralDistances.put("Naypyitaw", 320);
        centralDistances.put("Bago", 80);
        centralDistances.put("Pyay", 180);
        centralDistances.put("Taungoo", 250);
        centralDistances.put("Mawlamyine", 300);
        centralDistances.put("Pyin Oo Lwin", 700);
        centralDistances.put("Myitkyina", 1450);
        centralDistances.put("Lashio", 920);
        centralDistances.put("Kalay", 1040);
        
        int sourceDist = centralDistances.getOrDefault(sourceCity, 0);
        int destDist = centralDistances.getOrDefault(destinationCity, 0);
        
        return Math.abs(sourceDist - destDist);
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