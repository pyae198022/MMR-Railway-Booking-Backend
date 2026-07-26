package com.ticket.booking.service.impl;

import com.ticket.booking.dto.RouteDTO;
import com.ticket.booking.dto.StationDTO;
import com.ticket.booking.model.Route;
import com.ticket.booking.model.Station;
import com.ticket.booking.repository.RouteRepository;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    
    @Override
    public RouteDTO createRoute(RouteDTO routeDTO) {
        Route route = mapToEntity(routeDTO);
        Route savedRoute = routeRepository.save(route);
        return mapToDTO(savedRoute);
    }
    
    @Override
    public RouteDTO updateRoute(Long id, RouteDTO routeDTO) {
        Route existingRoute = routeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
        
        // Update route properties
        existingRoute.setRouteCode(routeDTO.getRouteCode());
        existingRoute.setRouteName(routeDTO.getRouteName());
        existingRoute.setDistanceKm(routeDTO.getDistanceKm());
        existingRoute.setEstimatedTravelTime(routeDTO.getEstimatedTravelTime());
        existingRoute.setRouteType(routeDTO.getRouteType());
        existingRoute.setDescription(routeDTO.getDescription());
        existingRoute.setStatus(routeDTO.getStatus());
        existingRoute.setBaseFare(routeDTO.getBaseFare());
        
        // Update stations
        Station startStation = stationRepository.findById(routeDTO.getStartStation().getId())
            .orElseThrow(() -> new RuntimeException("Start station not found"));
        Station endStation = stationRepository.findById(routeDTO.getEndStation().getId())
            .orElseThrow(() -> new RuntimeException("End station not found"));
        
        existingRoute.setStartStation(startStation);
        existingRoute.setEndStation(endStation);
        
        Route updatedRoute = routeRepository.save(existingRoute);
        return mapToDTO(updatedRoute);
    }
    
    @Override
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
    
    @Override
    public RouteDTO getRouteById(Long id) {
        Route route = routeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
        return mapToDTO(route);
    }
    
    @Override
    public RouteDTO getRouteByCode(String routeCode) {
        Route route = routeRepository.findByRouteCode(routeCode)
            .orElseThrow(() -> new RuntimeException("Route not found with code: " + routeCode));
        return mapToDTO(route);
    }
    
    @Override
    public List<RouteDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getRoutesByStartStation(Long stationId) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + stationId));
        return routeRepository.findByStartStation(station).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getRoutesByEndStation(Long stationId) {
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + stationId));
        return routeRepository.findByEndStation(station).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getRoutesBetweenStations(Long startStationId, Long endStationId) {
        Station startStation = stationRepository.findById(startStationId)
            .orElseThrow(() -> new RuntimeException("Start station not found"));
        Station endStation = stationRepository.findById(endStationId)
            .orElseThrow(() -> new RuntimeException("End station not found"));
        
        return routeRepository.findRoutesBetweenStations(startStationId, endStationId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getRoutesBetweenCities(String startCity, String endCity) {
        return routeRepository.findRoutesBetweenCities(startCity, endCity).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getRoutesByType(String routeType) {
        return routeRepository.findByRouteType(routeType).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> getActiveRoutes() {
        return routeRepository.findByStatus("ACTIVE").stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<RouteDTO> searchRoutes(String query) {
        // Search by route code, name, or station names
        List<Route> routes = routeRepository.findAll().stream()
            .filter(route -> 
                route.getRouteCode().toLowerCase().contains(query.toLowerCase()) ||
                route.getRouteName().toLowerCase().contains(query.toLowerCase()) ||
                route.getStartStation().getName().toLowerCase().contains(query.toLowerCase()) ||
                route.getEndStation().getName().toLowerCase().contains(query.toLowerCase()) ||
                route.getStartStation().getCity().toLowerCase().contains(query.toLowerCase()) ||
                route.getEndStation().getCity().toLowerCase().contains(query.toLowerCase())
            )
            .collect(Collectors.toList());
        
        return routes.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private Route mapToEntity(RouteDTO dto) {
        Route route = new Route();
        route.setId(dto.getId());
        route.setRouteCode(dto.getRouteCode());
        route.setRouteName(dto.getRouteName());
        route.setDistanceKm(dto.getDistanceKm());
        route.setEstimatedTravelTime(dto.getEstimatedTravelTime());
        route.setRouteType(dto.getRouteType());
        route.setDescription(dto.getDescription());
        route.setStatus(dto.getStatus());
        route.setBaseFare(dto.getBaseFare());
        
        // Set stations
        if (dto.getStartStation() != null && dto.getStartStation().getId() != null) {
            Station startStation = stationRepository.findById(dto.getStartStation().getId())
                .orElseThrow(() -> new RuntimeException("Start station not found"));
            route.setStartStation(startStation);
        }
        
        if (dto.getEndStation() != null && dto.getEndStation().getId() != null) {
            Station endStation = stationRepository.findById(dto.getEndStation().getId())
                .orElseThrow(() -> new RuntimeException("End station not found"));
            route.setEndStation(endStation);
        }
        
        return route;
    }
    
    private RouteDTO mapToDTO(Route route) {
        RouteDTO dto = new RouteDTO();
        dto.setId(route.getId());
        dto.setRouteCode(route.getRouteCode());
        dto.setRouteName(route.getRouteName());
        dto.setDistanceKm(route.getDistanceKm());
        dto.setEstimatedTravelTime(route.getEstimatedTravelTime());
        dto.setRouteType(route.getRouteType());
        dto.setDescription(route.getDescription());
        dto.setStatus(route.getStatus());
        dto.setBaseFare(route.getBaseFare());
        
        // Map start station
        if (route.getStartStation() != null) {
            Station startStation = route.getStartStation();
            StationDTO startStationDTO = new StationDTO(
                startStation.getId(),
                startStation.getCode(),
                startStation.getName(),
                startStation.getCity(),
                startStation.getState(),
                startStation.getPlatformCount(),
                startStation.getFacilities()
            );
            dto.setStartStation(startStationDTO);
        }
        
        // Map end station
        if (route.getEndStation() != null) {
            Station endStation = route.getEndStation();
            StationDTO endStationDTO = new StationDTO(
                endStation.getId(),
                endStation.getCode(),
                endStation.getName(),
                endStation.getCity(),
                endStation.getState(),
                endStation.getPlatformCount(),
                endStation.getFacilities()
            );
            dto.setEndStation(endStationDTO);
        }
        
        return dto;
    }
}