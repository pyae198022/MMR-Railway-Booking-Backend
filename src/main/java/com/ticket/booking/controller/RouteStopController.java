package com.ticket.booking.controller;

import com.ticket.booking.dto.RouteStopDTO;
import com.ticket.booking.service.RouteStopService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/route-stops")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RouteStopController {
    
    private final RouteStopService routeStopService;
    
    @PostMapping
    public ResponseEntity<RouteStopDTO> createRouteStop(@RequestBody RouteStopDTO routeStopDTO) {
        RouteStopDTO createdStop = routeStopService.createRouteStop(routeStopDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStop);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RouteStopDTO> updateRouteStop(@PathVariable Long id, @RequestBody RouteStopDTO routeStopDTO) {
        RouteStopDTO updatedStop = routeStopService.updateRouteStop(id, routeStopDTO);
        return ResponseEntity.ok(updatedStop);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouteStop(@PathVariable Long id) {
        routeStopService.deleteRouteStop(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RouteStopDTO> getRouteStopById(@PathVariable Long id) {
        RouteStopDTO routeStop = routeStopService.getRouteStopById(id);
        return ResponseEntity.ok(routeStop);
    }
    
    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<RouteStopDTO>> getAllRouteStopsByRouteId(@PathVariable Long routeId) {
        List<RouteStopDTO> routeStops = routeStopService.getAllRouteStopsByRouteId(routeId);
        return ResponseEntity.ok(routeStops);
    }
    
    @GetMapping("/route/{routeId}/active")
    public ResponseEntity<List<RouteStopDTO>> getActiveRouteStopsByRouteId(@PathVariable Long routeId) {
        List<RouteStopDTO> routeStops = routeStopService.getActiveRouteStopsByRouteId(routeId);
        return ResponseEntity.ok(routeStops);
    }
    
    @GetMapping("/route/{routeId}/range")
    public ResponseEntity<List<RouteStopDTO>> getRouteStopsByRange(
            @PathVariable Long routeId,
            @RequestParam Integer startOrder,
            @RequestParam Integer endOrder) {
        List<RouteStopDTO> routeStops = routeStopService.getRouteStopsByRouteIdAndStationRange(routeId, startOrder, endOrder);
        return ResponseEntity.ok(routeStops);
    }
    
    @GetMapping("/route/{routeId}/between-stations")
    public ResponseEntity<List<RouteStopDTO>> getStopsBetweenStations(
            @PathVariable Long routeId,
            @RequestParam Long startStationId,
            @RequestParam Long endStationId) {
        List<RouteStopDTO> routeStops = routeStopService.getStopsBetweenStations(routeId, startStationId, endStationId);
        return ResponseEntity.ok(routeStops);
    }
    
    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<RouteStopDTO>> getRouteStopsByStationId(@PathVariable Long stationId) {
        List<RouteStopDTO> routeStops = routeStopService.getRouteStopsByStationId(stationId);
        return ResponseEntity.ok(routeStops);
    }
    
    @PostMapping("/route/{routeId}/generate")
    public ResponseEntity<List<RouteStopDTO>> generateRouteStops(
            @PathVariable Long routeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime routeStartTime) {
        List<RouteStopDTO> generatedStops = routeStopService.generateRouteStopsForRoute(routeId, routeStartTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(generatedStops);
    }
    
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<RouteStopDTO>> getRouteStopsByTrainId(@PathVariable Long trainId) {
        // This would need integration with TrainService to get route from train
        // For now, return empty list or implement based on your train-route relationship
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<RouteStopDTO>> searchRouteStops(
            @RequestParam(required = false) Long routeId,
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) String stopType,
            @RequestParam(required = false) String status) {
        
        // This is a simplified search - in production, you'd have a more sophisticated search service
        if (routeId != null) {
            return ResponseEntity.ok(routeStopService.getAllRouteStopsByRouteId(routeId));
        } else if (stationId != null) {
            return ResponseEntity.ok(routeStopService.getRouteStopsByStationId(stationId));
        }
        
        return ResponseEntity.ok(List.of());
    }
}