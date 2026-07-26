package com.ticket.booking.controller;

import com.ticket.booking.model.Station;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.service.FareCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StationController {
    
    @Autowired
    private StationRepository stationRepository;
    
    @Autowired
    private FareCalculatorService fareCalculatorService;
    
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationRepository.findAll();
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/stations/code/{code}")
    public ResponseEntity<Station> getStationByCode(@PathVariable String code) {
        return stationRepository.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/stations/city/{city}")
    public ResponseEntity<List<Station>> getStationsByCity(@PathVariable String city) {
        List<Station> stations = stationRepository.findByCity(city);
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/state/{state}")
    public ResponseEntity<List<Station>> getStationsByState(@PathVariable String state) {
        List<Station> stations = stationRepository.findByState(state);
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/search")
    public ResponseEntity<List<Station>> searchStations(@RequestParam String query) {
        List<Station> stations = stationRepository.findByNameContainingIgnoreCase(query);
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/autocomplete")
    public ResponseEntity<List<Station>> autocompleteStations(@RequestParam String term) {
        List<Station> stations = stationRepository.findByNameContainingIgnoreCase(term);
        if (stations.size() > 10) {
            stations = stations.subList(0, 10); // Limit to 10 results
        }
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/info/{id}")
    public ResponseEntity<Map<String, Object>> getStationInfo(@PathVariable Long id) {
        return stationRepository.findById(id)
                .map(station -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("id", station.getId());
                    info.put("code", station.getCode());
                    info.put("name", station.getName());
                    info.put("city", station.getCity());
                    info.put("state", station.getState());
                    info.put("platformCount", station.getPlatformCount());
                    info.put("facilities", station.getFacilities());
                    
                    // Add additional info
                    Map<String, String> additionalInfo = getStationAdditionalInfo(station);
                    info.put("additionalInfo", additionalInfo);
                    
                    return ResponseEntity.ok(info);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/stations/popular")
    public ResponseEntity<List<Station>> getPopularStations() {
        List<Station> stations = stationRepository.findAll();
        // Return first 5 stations as popular (in reality, this would be based on booking data)
        if (stations.size() > 5) {
            stations = stations.subList(0, 5);
        }
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/stations/distance")
    public ResponseEntity<Map<String, Object>> getDistanceBetweenStations(
            @RequestParam Long fromStationId, 
            @RequestParam Long toStationId) {
        
        return stationRepository.findById(fromStationId)
                .flatMap(fromStation -> stationRepository.findById(toStationId)
                        .map(toStation -> {
                            int distance = fareCalculatorService.getApproximateDistance(
                                fromStation.getCity(),
                                toStation.getCity()
                            );
                            
                            Map<String, Object> result = new HashMap<>();
                            result.put("fromStation", fromStation);
                            result.put("toStation", toStation);
                            result.put("distanceKm", distance);
                            result.put("estimatedTravelTime", calculateTravelTime(distance));
                            
                            return ResponseEntity.ok(result);
                        }))
                .orElse(ResponseEntity.notFound().build());
    }
    
    private Map<String, String> getStationAdditionalInfo(Station station) {
        Map<String, String> info = new HashMap<>();
        
        // Add station-specific information based on station code
        switch (station.getCode()) {
            case "YGN":
                info.put("type", "Main Terminal");
                info.put("openingHours", "24/7");
                info.put("services", "VIP Lounge, Food Court, Currency Exchange, Tourist Information");
                info.put("contact", "Yangon Central Station, Tel: 01-xxxxxxx");
                break;
            case "MDY":
                info.put("type", "Main Terminal");
                info.put("openingHours", "5:00 AM - 10:00 PM");
                info.put("services", "Food Stalls, Souvenir Shops, Luggage Storage");
                info.put("contact", "Mandalay Station, Tel: 02-xxxxxxx");
                break;
            case "NPT":
                info.put("type", "Modern Terminal");
                info.put("openingHours", "6:00 AM - 9:00 PM");
                info.put("services", "WiFi, Conference Rooms, Modern Facilities");
                info.put("contact", "Naypyitaw Station, Tel: 067-xxxxxxx");
                break;
            case "BGN":
                info.put("type", "Major Station");
                info.put("openingHours", "6:00 AM - 8:00 PM");
                info.put("services", "Ticketing, Waiting Hall, Restrooms");
                info.put("contact", "Bago Station, Tel: 052-xxxxxxx");
                break;
            case "PYA":
                info.put("type", "Major Station");
                info.put("openingHours", "6:00 AM - 8:00 PM");
                info.put("services", "Ticketing, Waiting Area");
                info.put("contact", "Pyay Station, Tel: 053-xxxxxxx");
                break;
            default:
                info.put("type", "Regional Station");
                info.put("openingHours", "6:00 AM - 8:00 PM");
                info.put("services", "Basic Ticketing and Facilities");
                info.put("contact", "Check official website for details");
        }
        
        info.put("railwayLine", getRailwayLine(station.getCity()));
        info.put("notes", "Myanmar Railways Station - Official Code: " + station.getCode());
        
        return info;
    }
    
    private String getRailwayLine(String city) {
        // Determine which railway line this station belongs to
        switch (city) {
            case "Yangon":
            case "Bago":
            case "Pyay":
            case "Taungoo":
            case "Naypyitaw":
            case "Thazi":
            case "Mandalay":
                return "Yangon-Mandalay Main Line";
            case "Mawlamyine":
            case "Kyaikto":
            case "Thaton":
                return "Yangon-Mawlamyine Line";
            case "Myitkyina":
            case "Kalay":
            case "Monywa":
                return "Mandalay-Myitkyina Line";
            case "Pyin Oo Lwin":
            case "Lashio":
            case "Hsipaw":
                return "Mandalay-Lashio Line";
            default:
                return "Regional Railway Line";
        }
    }
    
    private String calculateTravelTime(int distanceKm) {
        if (distanceKm <= 100) {
            return "2-3 hours";
        } else if (distanceKm <= 300) {
            return "4-6 hours";
        } else if (distanceKm <= 600) {
            return "8-12 hours";
        } else if (distanceKm <= 1000) {
            return "12-18 hours";
        } else {
            return "18-24 hours";
        }
    }
}