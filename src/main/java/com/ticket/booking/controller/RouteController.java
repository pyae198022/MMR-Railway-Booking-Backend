package com.ticket.booking.controller;

import com.ticket.booking.dto.RouteDTO;
import com.ticket.booking.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RouteController {
    
    private final RouteService routeService;
    
    @PostMapping
    public ResponseEntity<RouteDTO> createRoute(@RequestBody RouteDTO routeDTO) {
        RouteDTO createdRoute = routeService.createRoute(routeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoute);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RouteDTO> updateRoute(@PathVariable Long id, @RequestBody RouteDTO routeDTO) {
        RouteDTO updatedRoute = routeService.updateRoute(id, routeDTO);
        return ResponseEntity.ok(updatedRoute);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RouteDTO> getRouteById(@PathVariable Long id) {
        RouteDTO route = routeService.getRouteById(id);
        return ResponseEntity.ok(route);
    }
    
    @GetMapping("/code/{routeCode}")
    public ResponseEntity<RouteDTO> getRouteByCode(@PathVariable String routeCode) {
        RouteDTO route = routeService.getRouteByCode(routeCode);
        return ResponseEntity.ok(route);
    }
    
    @GetMapping
    public ResponseEntity<List<RouteDTO>> getAllRoutes() {
        List<RouteDTO> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/start/{stationId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByStartStation(@PathVariable Long stationId) {
        List<RouteDTO> routes = routeService.getRoutesByStartStation(stationId);
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/end/{stationId}")
    public ResponseEntity<List<RouteDTO>> getRoutesByEndStation(@PathVariable Long stationId) {
        List<RouteDTO> routes = routeService.getRoutesByEndStation(stationId);
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/between-stations")
    public ResponseEntity<List<RouteDTO>> getRoutesBetweenStations(
            @RequestParam Long startStationId,
            @RequestParam Long endStationId) {
        List<RouteDTO> routes = routeService.getRoutesBetweenStations(startStationId, endStationId);
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/between-cities")
    public ResponseEntity<List<RouteDTO>> getRoutesBetweenCities(
            @RequestParam String startCity,
            @RequestParam String endCity) {
        List<RouteDTO> routes = routeService.getRoutesBetweenCities(startCity, endCity);
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/type/{routeType}")
    public ResponseEntity<List<RouteDTO>> getRoutesByType(@PathVariable String routeType) {
        List<RouteDTO> routes = routeService.getRoutesByType(routeType);
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<RouteDTO>> getActiveRoutes() {
        List<RouteDTO> routes = routeService.getActiveRoutes();
        return ResponseEntity.ok(routes);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<RouteDTO>> searchRoutes(@RequestParam String query) {
        List<RouteDTO> routes = routeService.searchRoutes(query);
        return ResponseEntity.ok(routes);
    }
}