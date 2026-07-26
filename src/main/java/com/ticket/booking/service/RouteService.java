package com.ticket.booking.service;

import com.ticket.booking.dto.RouteDTO;
import java.util.List;

public interface RouteService {
    
    RouteDTO createRoute(RouteDTO routeDTO);
    
    RouteDTO updateRoute(Long id, RouteDTO routeDTO);
    
    void deleteRoute(Long id);
    
    RouteDTO getRouteById(Long id);
    
    RouteDTO getRouteByCode(String routeCode);
    
    List<RouteDTO> getAllRoutes();
    
    List<RouteDTO> getRoutesByStartStation(Long stationId);
    
    List<RouteDTO> getRoutesByEndStation(Long stationId);
    
    List<RouteDTO> getRoutesBetweenStations(Long startStationId, Long endStationId);
    
    List<RouteDTO> getRoutesBetweenCities(String startCity, String endCity);
    
    List<RouteDTO> getRoutesByType(String routeType);
    
    List<RouteDTO> getActiveRoutes();
    
    List<RouteDTO> searchRoutes(String query);
}