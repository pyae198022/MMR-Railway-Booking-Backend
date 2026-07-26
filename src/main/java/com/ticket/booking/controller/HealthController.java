package com.ticket.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        return Map.of(
            "status", "UP",
            "service", "MMR Railway Booking Backend",
            "timestamp", LocalDateTime.now(),
            "version", "1.0.0"
        );
    }
    
    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "name", "MMR Railway Booking Backend API",
            "description", "Backend API for MMR Railway Booking System",
            "endpoints", Map.of(
                "stations", "/api/stations",
                "trains", "/api/trains",
                "bookings", "/api/bookings",
                "search", "/api/trains/search"
            )
        );
    }
}