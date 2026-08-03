package com.ticket.booking.controller;

import com.ticket.booking.service.MockRouteService;
import com.ticket.booking.service.MockPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mock")
@CrossOrigin(origins = "*")
public class MockRouteController {
    
    @Autowired
    private MockRouteService mockRouteService;
    
    @Autowired
    private MockPaymentService mockPaymentService;
    
    /**
     * Get popular Myanmar railway routes
     */
    @GetMapping("/routes/popular")
    public ResponseEntity<List<Map<String, Object>>> getPopularRoutes() {
        List<Map<String, Object>> popularRoutes = mockRouteService.getPopularRoutes();
        return ResponseEntity.ok(popularRoutes);
    }
    
    /**
     * Get popular routes with exactly 5 trains for each route
     */
    @GetMapping("/routes/popular-with-trains")
    public ResponseEntity<List<Map<String, Object>>> getPopularRoutesWithTrains() {
        List<Map<String, Object>> popularRoutesWithTrains = mockRouteService.getPopularRoutesWithTrains();
        return ResponseEntity.ok(popularRoutesWithTrains);
    }
    
    /**
     * Search for routes between cities with mock data
     */
    @PostMapping("/routes/search")
    public ResponseEntity<List<Map<String, Object>>> searchRoutes(
            @RequestBody Map<String, Object> searchRequest) {
        
        try {
            String sourceCity = (String) searchRequest.get("sourceCity");
            String destinationCity = (String) searchRequest.get("destinationCity");
            String journeyDateStr = (String) searchRequest.get("journeyDate");
            Integer numberOfPassengers = (Integer) searchRequest.get("numberOfPassengers");
            
            if (sourceCity == null || destinationCity == null || journeyDateStr == null) {
                return ResponseEntity.badRequest().build();
            }
            
            LocalDateTime journeyDate = LocalDateTime.parse(journeyDateStr);
            int passengers = numberOfPassengers != null ? numberOfPassengers : 1;
            
            List<Map<String, Object>> searchResults = mockRouteService.searchRoutes(
                sourceCity, destinationCity, journeyDate, passengers
            );
            
            return ResponseEntity.ok(searchResults);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid search request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
    
    /**
     * Get available payment methods for Myanmar
     */
    @GetMapping("/payments/methods")
    public ResponseEntity<List<Map<String, Object>>> getPaymentMethods() {
        List<Map<String, Object>> paymentMethods = mockPaymentService.getAvailablePaymentMethods();
        return ResponseEntity.ok(paymentMethods);
    }
    
    /**
     * Initialize a mock payment
     */
    @PostMapping("/payments/initialize")
    public ResponseEntity<Map<String, Object>> initializePayment(@RequestBody Map<String, Object> paymentRequest) {
        try {
            Double amount = ((Number) paymentRequest.get("amount")).doubleValue();
            String currency = (String) paymentRequest.getOrDefault("currency", "MMK");
            String paymentMethod = (String) paymentRequest.get("paymentMethod");
            String bookingReference = (String) paymentRequest.get("bookingReference");
            
            Map<String, Object> paymentInit = mockPaymentService.initializePayment(
                amount, currency, paymentMethod, bookingReference
            );
            
            return ResponseEntity.ok(paymentInit);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid payment initialization request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Process a mock payment
     */
    @PostMapping("/payments/process")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> processRequest) {
        try {
            String paymentId = (String) processRequest.get("paymentId");
            String paymentMethod = (String) processRequest.get("paymentMethod");
            Map<String, Object> paymentDetails = (Map<String, Object>) processRequest.get("paymentDetails");
            
            Map<String, Object> paymentResult = mockPaymentService.processPayment(
                paymentId, paymentMethod, paymentDetails
            );
            
            return ResponseEntity.ok(paymentResult);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid payment process request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Verify a payment status
     */
    @GetMapping("/payments/verify/{paymentId}")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @PathVariable String paymentId,
            @RequestParam(required = false) String transactionReference) {
        
        Map<String, Object> verification = mockPaymentService.verifyPayment(paymentId, transactionReference);
        return ResponseEntity.ok(verification);
    }
    
    /**
     * Calculate payment breakdown
     */
    @PostMapping("/payments/calculate")
    public ResponseEntity<Map<String, Object>> calculatePaymentBreakdown(@RequestBody Map<String, Object> request) {
        try {
            Double amount = ((Number) request.get("amount")).doubleValue();
            String paymentMethod = (String) request.get("paymentMethod");
            
            Map<String, Object> breakdown = mockPaymentService.calculatePaymentBreakdown(amount, paymentMethod);
            return ResponseEntity.ok(breakdown);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid calculation request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get payment status history
     */
    @GetMapping("/payments/history/{paymentId}")
    public ResponseEntity<List<Map<String, Object>>> getPaymentHistory(@PathVariable String paymentId) {
        List<Map<String, Object>> history = mockPaymentService.getPaymentStatusHistory(paymentId);
        return ResponseEntity.ok(history);
    }
    
    /**
     * Refund a payment
     */
    @PostMapping("/payments/refund")
    public ResponseEntity<Map<String, Object>> refundPayment(@RequestBody Map<String, Object> refundRequest) {
        try {
            String paymentId = (String) refundRequest.get("paymentId");
            Double amount = ((Number) refundRequest.get("amount")).doubleValue();
            String reason = (String) refundRequest.getOrDefault("reason", "Customer request");
            
            Map<String, Object> refund = mockPaymentService.refundPayment(paymentId, amount, reason);
            return ResponseEntity.ok(refund);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid refund request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Generate mock routes between all stations (admin endpoint)
     */
    @PostMapping("/routes/generate-all")
    public ResponseEntity<Map<String, Object>> generateAllRoutes() {
        mockRouteService.generateAllMockRoutes();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mock routes generated successfully");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Search routes with multiple options
     */
    @PostMapping("/routes/search/advanced")
    public ResponseEntity<List<Map<String, Object>>> advancedSearch(
            @RequestBody Map<String, Object> advancedRequest) {
        
        try {
            String sourceCity = (String) advancedRequest.get("sourceCity");
            String destinationCity = (String) advancedRequest.get("destinationCity");
            String journeyDateStr = (String) advancedRequest.get("journeyDate");
            Integer numberOfPassengers = (Integer) advancedRequest.get("numberOfPassengers");
            String trainType = (String) advancedRequest.get("trainType");
            String sortBy = (String) advancedRequest.get("sortBy"); // departure, arrival, price, duration
            
            if (sourceCity == null || destinationCity == null || journeyDateStr == null) {
                return ResponseEntity.badRequest().build();
            }
            
            LocalDateTime journeyDate = LocalDateTime.parse(journeyDateStr);
            int passengers = numberOfPassengers != null ? numberOfPassengers : 1;
            
            List<Map<String, Object>> searchResults = mockRouteService.searchRoutes(
                sourceCity, destinationCity, journeyDate, passengers
            );
            
            // Apply filters if specified
            if (trainType != null && !trainType.isEmpty()) {
                searchResults.removeIf(result -> {
                    Map<String, Object> train = (Map<String, Object>) result.get("train");
                    return !trainType.equalsIgnoreCase((String) train.get("trainType"));
                });
            }
            
            // Apply sorting if specified
            if (sortBy != null && !sortBy.isEmpty()) {
                searchResults.sort((a, b) -> {
                    switch (sortBy.toLowerCase()) {
                        case "departure":
                            Map<String, Object> trainA = (Map<String, Object>) a.get("train");
                            Map<String, Object> trainB = (Map<String, Object>) b.get("train");
                            return ((String) trainA.get("departureTime"))
                                    .compareTo((String) trainB.get("departureTime"));
                        case "price":
                            Double priceA = (Double) a.get("farePerPassenger");
                            Double priceB = (Double) b.get("farePerPassenger");
                            return priceA.compareTo(priceB);
                        case "duration":
                            // Extract hours from duration string "X hours"
                            String durationA = (String) a.get("travelDuration");
                            String durationB = (String) b.get("travelDuration");
                            int hoursA = Integer.parseInt(durationA.split(" ")[0]);
                            int hoursB = Integer.parseInt(durationB.split(" ")[0]);
                            return Integer.compare(hoursA, hoursB);
                        default:
                            return 0;
                    }
                });
            }
            
            return ResponseEntity.ok(searchResults);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid advanced search request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(List.of(error));
        }
    }
    
    /**
     * Health check for mock services
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("service", "Mock Routes & Payment API");
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        health.put("features", List.of("Mock Routes", "Payment Processing", "Refunds", "Payment Methods"));
        
        return ResponseEntity.ok(health);
    }
}